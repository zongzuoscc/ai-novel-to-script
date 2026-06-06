package com.novel2script.backend.story;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.novel2script.backend.ai.AiChatClient;
import com.novel2script.backend.source.SourceChapter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
public class AiStoryAssetExtractor {

    private static final int MAX_STORY_CHARS = 12000;

    private final AiChatClient aiChatClient;

    private final ObjectMapper objectMapper;

    public AiStoryAssetExtractor(AiChatClient aiChatClient, ObjectMapper objectMapper) {
        this.aiChatClient = aiChatClient;
        this.objectMapper = objectMapper;
    }

    public Result extract(String projectId, List<SourceChapter> chapters) {
        try {
            String response = aiChatClient.completeJson(
                    "你是小说剧本化系统的故事资产分析助手。只返回合法 JSON，不要输出解释。",
                    buildPrompt(chapters)
            );
            JsonNode root = objectMapper.readTree(response);
            List<StoryEntity> entities = parseEntities(projectId, root.path("entities"));
            List<StoryEvent> events = parseEvents(projectId, chapters, root.path("events"));
            if (entities.isEmpty() || events.isEmpty()) {
                throw new IllegalStateException("AI 故事资产为空");
            }
            return new Result(entities, events);
        } catch (Exception ex) {
            throw new IllegalStateException("AI 故事资产抽取失败", ex);
        }
    }

    private List<StoryEntity> parseEntities(String projectId, JsonNode entitiesNode) {
        List<StoryEntity> entities = new ArrayList<>();
        int characterIndex = 1;
        int locationIndex = 1;
        if (!entitiesNode.isArray()) {
            return entities;
        }
        for (JsonNode node : entitiesNode) {
            StoryEntityType type = parseEntityType(node.path("entityType").asText());
            String entityId = type == StoryEntityType.LOCATION
                    ? "L" + String.format(Locale.ROOT, "%03d", locationIndex++)
                    : "C" + String.format(Locale.ROOT, "%03d", characterIndex++);
            String canonicalName = normalizeText(node.path("canonicalName").asText(), type == StoryEntityType.LOCATION ? "未命名地点" : "未命名角色");
            entities.add(new StoryEntity(
                    projectId,
                    entityId,
                    type,
                    canonicalName,
                    writeJson(readStringArray(node.path("aliases"), List.of(canonicalName))),
                    normalizeText(node.path("profile").asText(), "暂无说明"),
                    writeJson(readStringArray(node.path("sourceRefs"), List.of()))
            ));
        }
        return entities;
    }

    private List<StoryEvent> parseEvents(String projectId, List<SourceChapter> chapters, JsonNode eventsNode) {
        List<StoryEvent> events = new ArrayList<>();
        if (!eventsNode.isArray()) {
            return events;
        }
        int index = 1;
        for (JsonNode node : eventsNode) {
            int chapterNo = Math.max(1, node.path("chapterNo").asInt(index));
            SourceChapter chapter = findChapterByNo(chapters, chapterNo);
            String eventId = "E" + String.format(Locale.ROOT, "%03d", index);
            events.add(new StoryEvent(
                    projectId,
                    eventId,
                    chapter.getId(),
                    index,
                    normalizeText(node.path("title").asText(), chapter.getTitle()),
                    normalizeText(node.path("summary").asText(), chapter.getSummary() == null ? chapter.getTitle() : chapter.getSummary()),
                    writeJson(readStringArray(node.path("sourceRefs"), List.of("ch" + chapter.getChapterNo())))
            ));
            index++;
        }
        return events;
    }

    private StoryEntityType parseEntityType(String value) {
        return "LOCATION".equalsIgnoreCase(value) ? StoryEntityType.LOCATION : StoryEntityType.CHARACTER;
    }

    private SourceChapter findChapterByNo(List<SourceChapter> chapters, int chapterNo) {
        return chapters.stream()
                .filter(chapter -> chapter.getChapterNo() == chapterNo)
                .findFirst()
                .orElse(chapters.get(0));
    }

    private List<String> readStringArray(JsonNode node, List<String> fallback) {
        if (!node.isArray()) {
            return fallback;
        }
        List<String> values = new ArrayList<>();
        for (JsonNode item : node) {
            String value = item.asText();
            if (value != null && !value.isBlank()) {
                values.add(value.trim());
            }
        }
        return values.isEmpty() ? fallback : values;
    }

    private String normalizeText(String value, String fallback) {
        return value == null || value.isBlank() ? fallback : value.trim();
    }

    private String writeJson(List<String> values) {
        try {
            return objectMapper.writeValueAsString(values);
        } catch (Exception ex) {
            throw new IllegalStateException("序列化 AI 故事资产失败", ex);
        }
    }

    private String buildPrompt(List<SourceChapter> chapters) {
        return """
                请从小说文本中抽取剧本改编需要的故事资产。
                只返回 JSON，格式如下：
                {
                  "entities": [
                    {
                      "entityType": "CHARACTER 或 LOCATION",
                      "canonicalName": "名称",
                      "aliases": ["别名"],
                      "profile": "人物小传或地点说明",
                      "sourceRefs": ["ch1"]
                    }
                  ],
                  "events": [
                    {
                      "chapterNo": 1,
                      "title": "事件标题",
                      "summary": "事件摘要",
                      "sourceRefs": ["ch1"]
                    }
                  ]
                }
                要求：
                1. 角色和地点总数控制在 4 到 12 个。
                2. 事件按故事发生顺序排列，每章至少 1 个关键事件。
                3. sourceRefs 只使用 ch + 章节号，例如 ch1。

                小说文本：
                %s
                """.formatted(buildChapterText(chapters));
    }

    private String buildChapterText(List<SourceChapter> chapters) {
        StringBuilder builder = new StringBuilder();
        for (SourceChapter chapter : chapters) {
            builder.append("ch").append(chapter.getChapterNo())
                    .append(" ").append(chapter.getTitle()).append('\n')
                    .append(chapter.getCleanText()).append("\n\n");
            if (builder.length() >= MAX_STORY_CHARS) {
                return builder.substring(0, MAX_STORY_CHARS);
            }
        }
        return builder.toString();
    }

    public record Result(List<StoryEntity> entities, List<StoryEvent> events) {
    }
}
