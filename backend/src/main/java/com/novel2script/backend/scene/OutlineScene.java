package com.novel2script.backend.scene;

import java.time.LocalDateTime;

public class OutlineScene {

    private Long id;
    private String projectId;
    private String sceneId;
    private Integer seqNo;
    private String title;
    private String intExt;
    private String locationId;
    private String timeOfDay;
    private String purposePlot;
    private String purposeCharacter;
    private String charactersJson;
    private String sourceRefsJson;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public OutlineScene() {
    }

    public OutlineScene(String projectId, String sceneId, Integer seqNo, String title, String intExt, String locationId, String timeOfDay, String purposePlot, String purposeCharacter, String charactersJson, String sourceRefsJson, String status) {
        this.projectId = projectId;
        this.sceneId = sceneId;
        this.seqNo = seqNo;
        this.title = title;
        this.intExt = intExt;
        this.locationId = locationId;
        this.timeOfDay = timeOfDay;
        this.purposePlot = purposePlot;
        this.purposeCharacter = purposeCharacter;
        this.charactersJson = charactersJson;
        this.sourceRefsJson = sourceRefsJson;
        this.status = status;
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

    public String getIntExt() {
        return intExt;
    }

    public void setIntExt(String intExt) {
        this.intExt = intExt;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }

    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public String getPurposePlot() {
        return purposePlot;
    }

    public void setPurposePlot(String purposePlot) {
        this.purposePlot = purposePlot;
    }

    public String getPurposeCharacter() {
        return purposeCharacter;
    }

    public void setPurposeCharacter(String purposeCharacter) {
        this.purposeCharacter = purposeCharacter;
    }

    public String getCharactersJson() {
        return charactersJson;
    }

    public void setCharactersJson(String charactersJson) {
        this.charactersJson = charactersJson;
    }

    public String getSourceRefsJson() {
        return sourceRefsJson;
    }

    public void setSourceRefsJson(String sourceRefsJson) {
        this.sourceRefsJson = sourceRefsJson;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
