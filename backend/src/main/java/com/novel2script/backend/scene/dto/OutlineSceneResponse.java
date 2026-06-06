package com.novel2script.backend.scene.dto;

import com.novel2script.backend.scene.OutlineScene;

import java.util.List;

public class OutlineSceneResponse {

    private String sceneId;
    private Integer seqNo;
    private String title;
    private SluglineResponse slugline;
    private PurposeResponse purpose;
    private List<String> characters;
    private List<String> sourceRefs;
    private String status;

    public OutlineSceneResponse(String sceneId, Integer seqNo, String title, SluglineResponse slugline, PurposeResponse purpose, List<String> characters, List<String> sourceRefs, String status) {
        this.sceneId = sceneId;
        this.seqNo = seqNo;
        this.title = title;
        this.slugline = slugline;
        this.purpose = purpose;
        this.characters = characters;
        this.sourceRefs = sourceRefs;
        this.status = status;
    }

    public static OutlineSceneResponse from(OutlineScene scene, List<String> characters, List<String> sourceRefs) {
        return new OutlineSceneResponse(
                scene.getSceneId(),
                scene.getSeqNo(),
                scene.getTitle(),
                new SluglineResponse(scene.getIntExt(), scene.getLocationId(), scene.getTimeOfDay()),
                new PurposeResponse(scene.getPurposePlot(), scene.getPurposeCharacter()),
                characters,
                sourceRefs,
                scene.getStatus()
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

    public SluglineResponse getSlugline() {
        return slugline;
    }

    public PurposeResponse getPurpose() {
        return purpose;
    }

    public List<String> getCharacters() {
        return characters;
    }

    public List<String> getSourceRefs() {
        return sourceRefs;
    }

    public String getStatus() {
        return status;
    }

    public record SluglineResponse(String intExt, String locationId, String timeOfDay) {
    }

    public record PurposeResponse(String plot, String character) {
    }
}
