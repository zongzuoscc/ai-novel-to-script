package com.novel2script.backend.workflow.dto;

import java.time.LocalDateTime;

/**
 * 长任务响应。用于前端提交异步分析/生成任务后展示任务状态。
 */
public class WorkflowJobResponse {

    private String jobId;

    private String projectId;

    private String jobType;

    private String status;

    private String message;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public WorkflowJobResponse() {
    }

    public WorkflowJobResponse(
            String jobId,
            String projectId,
            String jobType,
            String status,
            String message,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.jobId = jobId;
        this.projectId = projectId;
        this.jobType = jobType;
        this.status = status;
        this.message = message;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
