package com.novel2script.backend.workflow;

import com.novel2script.backend.project.ProjectService;
import com.novel2script.backend.scene.SceneGenerationService;
import com.novel2script.backend.story.StoryAnalysisService;
import com.novel2script.backend.workflow.dto.WorkflowJobResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class WorkflowJobService {

    private static final Logger log = LoggerFactory.getLogger(WorkflowJobService.class);

    private final StoryAnalysisService storyAnalysisService;

    private final SceneGenerationService sceneGenerationService;

    private final ProjectService projectService;

    private final ProgressEventPublisher progressEventPublisher;

    private final ExecutorService executorService = Executors.newFixedThreadPool(2);

    private final Map<String, JobState> jobs = new ConcurrentHashMap<>();

    public WorkflowJobService(
            StoryAnalysisService storyAnalysisService,
            SceneGenerationService sceneGenerationService,
            ProjectService projectService,
            ProgressEventPublisher progressEventPublisher
    ) {
        this.storyAnalysisService = storyAnalysisService;
        this.sceneGenerationService = sceneGenerationService;
        this.projectService = projectService;
        this.progressEventPublisher = progressEventPublisher;
    }

    public WorkflowJobResponse submitStoryAnalysis(String projectId) {
        return submit(projectId, "story_analysis_async", "故事资产分析任务已提交", () -> storyAnalysisService.analyze(projectId));
    }

    public WorkflowJobResponse submitIncrementalStoryAnalysis(String projectId) {
        return submit(projectId, "story_analysis_incremental_async", "增量故事资产分析任务已提交", () -> storyAnalysisService.analyzeIncremental(projectId));
    }

    public WorkflowJobResponse submitOutlineGeneration(String projectId) {
        return submit(projectId, "outline_generation_async", "场景大纲生成任务已提交", () -> sceneGenerationService.listOutline(projectId));
    }

    public WorkflowJobResponse submitIncrementalOutlineGeneration(String projectId) {
        return submit(projectId, "outline_generation_incremental_async", "增量场景大纲生成任务已提交", () -> sceneGenerationService.generateIncrementalOutline(projectId));
    }

    public WorkflowJobResponse getJob(String projectId, String jobId) {
        JobState job = jobs.get(jobId);
        if (job == null || !projectId.equals(job.projectId())) {
            throw new IllegalArgumentException("任务不存在: " + jobId);
        }
        return job.toResponse();
    }

    private WorkflowJobResponse submit(String projectId, String jobType, String submitMessage, Runnable runnable) {
        projectService.getProjectEntity(projectId);
        String jobId = "job_" + UUID.randomUUID().toString().replace("-", "");
        JobState job = new JobState(jobId, projectId, jobType, "RUNNING", submitMessage, LocalDateTime.now(), LocalDateTime.now());
        jobs.put(jobId, job);
        progressEventPublisher.jobStarted(projectId, jobType, "queued", 1, submitMessage);

        CompletableFuture.runAsync(() -> runJob(jobId, runnable), executorService);
        return job.toResponse();
    }

    private void runJob(String jobId, Runnable runnable) {
        JobState job = jobs.get(jobId);
        if (job == null) {
            return;
        }
        try {
            update(jobId, "RUNNING", "任务正在后台执行");
            runnable.run();
            update(jobId, "SUCCEEDED", "任务执行完成");
            progressEventPublisher.jobCompleted(job.projectId(), "job_finished", 100, false, job.jobType() + " 执行完成");
            log.info("异步任务执行完成: projectId={}, jobId={}, jobType={}", job.projectId(), jobId, job.jobType());
        } catch (Exception ex) {
            String message = rootCauseMessage(ex);
            update(jobId, "FAILED", message);
            progressEventPublisher.jobFailed(job.projectId(), "job_failed", job.jobType(), message);
            log.warn("异步任务执行失败: projectId={}, jobId={}, jobType={}, reason={}", job.projectId(), jobId, job.jobType(), message);
        }
    }

    private void update(String jobId, String status, String message) {
        jobs.computeIfPresent(jobId, (ignored, current) -> current.withStatus(status, message));
    }

    private String rootCauseMessage(Exception ex) {
        Throwable current = ex;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        String message = current.getMessage();
        if (message == null || message.isBlank()) {
            return current.getClass().getSimpleName();
        }
        return message.length() > 160 ? message.substring(0, 160) : message;
    }

    private record JobState(
            String jobId,
            String projectId,
            String jobType,
            String status,
            String message,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        private JobState withStatus(String nextStatus, String nextMessage) {
            return new JobState(jobId, projectId, jobType, nextStatus, nextMessage, createdAt, LocalDateTime.now());
        }

        private WorkflowJobResponse toResponse() {
            return new WorkflowJobResponse(jobId, projectId, jobType, status, message, createdAt, updatedAt);
        }
    }
}
