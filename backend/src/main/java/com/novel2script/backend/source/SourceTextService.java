package com.novel2script.backend.source;

import com.novel2script.backend.common.ProjectOperationLock;
import com.novel2script.backend.project.Project;
import com.novel2script.backend.project.ProjectService;
import com.novel2script.backend.project.ProjectStatus;
import com.novel2script.backend.scene.OutlineSceneMapper;
import com.novel2script.backend.scene.SceneScriptMapper;
import com.novel2script.backend.source.dto.ChapterResponse;
import com.novel2script.backend.source.dto.SubmitSourceRequest;
import com.novel2script.backend.story.StoryEntityMapper;
import com.novel2script.backend.story.StoryEventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SourceTextService {

    private static final Logger log = LoggerFactory.getLogger(SourceTextService.class);

    private final ProjectService projectService;

    private final SourceChapterMapper sourceChapterMapper;

    private final StoryEntityMapper storyEntityMapper;

    private final StoryEventMapper storyEventMapper;

    private final OutlineSceneMapper outlineSceneMapper;

    private final SceneScriptMapper sceneScriptMapper;

    private final ChapterSplitter chapterSplitter;

    private final ProjectOperationLock projectOperationLock;

    public SourceTextService(
            ProjectService projectService,
            SourceChapterMapper sourceChapterMapper,
            StoryEntityMapper storyEntityMapper,
            StoryEventMapper storyEventMapper,
            OutlineSceneMapper outlineSceneMapper,
            SceneScriptMapper sceneScriptMapper,
            ChapterSplitter chapterSplitter,
            ProjectOperationLock projectOperationLock
    ) {
        this.projectService = projectService;
        this.sourceChapterMapper = sourceChapterMapper;
        this.storyEntityMapper = storyEntityMapper;
        this.storyEventMapper = storyEventMapper;
        this.outlineSceneMapper = outlineSceneMapper;
        this.sceneScriptMapper = sceneScriptMapper;
        this.chapterSplitter = chapterSplitter;
        this.projectOperationLock = projectOperationLock;
    }

    @Transactional
    public List<ChapterResponse> submitSource(String projectId, SubmitSourceRequest request) {
        return projectOperationLock.execute(projectId, () -> submitSourceLocked(projectId, request));
    }

    private List<ChapterResponse> submitSourceLocked(String projectId, SubmitSourceRequest request) {
        long startedAt = System.currentTimeMillis();
        log.info("开始提交小说正文: projectId={}", projectId);
        Project project = projectService.getProjectEntity(projectId);
        projectService.updateStatus(projectId, ProjectStatus.SOURCE_SUBMITTED);

        List<ChapterSplitter.ChapterSegment> segments = chapterSplitter.split(request.getContent());
        sceneScriptMapper.deleteByProjectId(projectId);
        outlineSceneMapper.deleteByProjectId(projectId);
        storyEntityMapper.deleteByProjectId(projectId);
        storyEventMapper.deleteByProjectId(projectId);
        sourceChapterMapper.deleteByProjectId(projectId);

        List<SourceChapter> chapters = segments.stream()
                .map(segment -> new SourceChapter(
                        project,
                        segment.chapterNo(),
                        segment.title(),
                        segment.rawText(),
                        segment.cleanText()
                ))
                .toList();

        if (!chapters.isEmpty()) {
            sourceChapterMapper.insertBatch(chapters);
        }
        projectService.updateStatus(projectId, ProjectStatus.CHAPTERED);
        log.info(
                "小说正文提交完成: projectId={}, chapterCount={}, elapsedMs={}",
                projectId,
                chapters.size(),
                System.currentTimeMillis() - startedAt
        );

        return sourceChapterMapper.findByProjectIdOrderByChapterNoAsc(projectId).stream()
                .map(ChapterResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ChapterResponse> listChapters(String projectId) {
        projectService.getProjectEntity(projectId);
        return sourceChapterMapper.findByProjectIdOrderByChapterNoAsc(projectId).stream()
                .map(ChapterResponse::from)
                .toList();
    }
}
