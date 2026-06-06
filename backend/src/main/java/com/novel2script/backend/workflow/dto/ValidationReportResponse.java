package com.novel2script.backend.workflow.dto;

import java.util.List;

public class ValidationReportResponse {

    private String projectId;
    private String status;
    private List<ValidationItemResponse> items;

    public ValidationReportResponse(String projectId, String status, List<ValidationItemResponse> items) {
        this.projectId = projectId;
        this.status = status;
        this.items = items;
    }

    public String getProjectId() {
        return projectId;
    }

    public String getStatus() {
        return status;
    }

    public List<ValidationItemResponse> getItems() {
        return items;
    }

    public record ValidationItemResponse(String sceneId, String level, String field, String message) {
    }
}
