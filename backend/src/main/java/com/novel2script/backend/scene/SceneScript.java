package com.novel2script.backend.scene;

import java.time.LocalDateTime;

public class SceneScript {

    private Long id;
    private String projectId;
    private String sceneId;
    private Integer seqNo;
    private String title;
    private String actionJson;
    private String dialogueJson;
    private String sourceRefsJson;
    private String validationStatus;
    private String warningsJson;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public SceneScript() {
    }

    public SceneScript(String projectId, String sceneId, Integer seqNo, String title, String actionJson, String dialogueJson, String sourceRefsJson, String validationStatus, String warningsJson) {
        this.projectId = projectId;
        this.sceneId = sceneId;
        this.seqNo = seqNo;
        this.title = title;
        this.actionJson = actionJson;
        this.dialogueJson = dialogueJson;
        this.sourceRefsJson = sourceRefsJson;
        this.validationStatus = validationStatus;
        this.warningsJson = warningsJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(Integer seqNo) {
        this.seqNo = seqNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getActionJson() {
        return actionJson;
    }

    public void setActionJson(String actionJson) {
        this.actionJson = actionJson;
    }

    public String getDialogueJson() {
        return dialogueJson;
    }

    public void setDialogueJson(String dialogueJson) {
        this.dialogueJson = dialogueJson;
    }

    public String getSourceRefsJson() {
        return sourceRefsJson;
    }

    public void setSourceRefsJson(String sourceRefsJson) {
        this.sourceRefsJson = sourceRefsJson;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public void setValidationStatus(String validationStatus) {
        this.validationStatus = validationStatus;
    }

    public String getWarningsJson() {
        return warningsJson;
    }

    public void setWarningsJson(String warningsJson) {
        this.warningsJson = warningsJson;
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
