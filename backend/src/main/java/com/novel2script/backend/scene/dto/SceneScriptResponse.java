package com.novel2script.backend.scene.dto;

import com.novel2script.backend.scene.SceneScript;

import java.util.List;

public class SceneScriptResponse {

    private String sceneId;
    private Integer seqNo;
    private String title;
    private List<String> action;
    private List<DialogueResponse> dialogue;
    private List<String> sourceRefs;
    private String validationStatus;
    private List<String> warnings;

    public SceneScriptResponse(String sceneId, Integer seqNo, String title, List<String> action, List<DialogueResponse> dialogue, List<String> sourceRefs, String validationStatus, List<String> warnings) {
        this.sceneId = sceneId;
        this.seqNo = seqNo;
        this.title = title;
        this.action = action;
        this.dialogue = dialogue;
        this.sourceRefs = sourceRefs;
        this.validationStatus = validationStatus;
        this.warnings = warnings;
    }

    public static SceneScriptResponse from(SceneScript sceneScript, List<String> action, List<DialogueResponse> dialogue, List<String> sourceRefs, List<String> warnings) {
        return new SceneScriptResponse(
                sceneScript.getSceneId(),
                sceneScript.getSeqNo(),
                sceneScript.getTitle(),
                action,
                dialogue,
                sourceRefs,
                sceneScript.getValidationStatus(),
                warnings
        );
    }

    public String getSceneId() {
        return sceneId;
    }

    public Integer getSeqNo() {
        return seqNo;
    }

    public String getTitle() {
        return title;
    }

    public List<String> getAction() {
        return action;
    }

    public List<DialogueResponse> getDialogue() {
        return dialogue;
    }

    public List<String> getSourceRefs() {
        return sourceRefs;
    }

    public String getValidationStatus() {
        return validationStatus;
    }

    public List<String> getWarnings() {
        return warnings;
    }

    public record DialogueResponse(String characterId, String line) {
    }
}
