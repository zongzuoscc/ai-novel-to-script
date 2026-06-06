package com.novel2script.backend.source;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 本地规则版章节摘要生成器。
 * 作用是先稳定打通摘要字段和接口；接入大模型时，只需要替换该生成器实现。
 */
@Component
public class RuleBasedChapterSummaryGenerator implements ChapterSummaryGenerator {

    private static final int MAX_SUMMARY_LENGTH = 180;

    @Override
    public String generate(SourceChapter chapter) {
        String content = removeTitle(chapter.getTitle(), chapter.getCleanText());
        List<String> sentences = splitSentences(content);
        if (sentences.isEmpty()) {
            return chapter.getTitle();
        }

        StringBuilder summary = new StringBuilder();
        for (String sentence : sentences) {
            if (sentence.isBlank()) {
                continue;
            }
            if (summary.length() > 0) {
                summary.append(" ");
            }
            summary.append(sentence.trim());
            if (summary.length() >= MAX_SUMMARY_LENGTH || summary.length() > 80 && sentence.endsWith("。")) {
                break;
            }
        }
        return trimToLength(summary.toString(), MAX_SUMMARY_LENGTH);
    }

    private String removeTitle(String title, String cleanText) {
        if (cleanText == null || cleanText.isBlank()) {
            return "";
        }
        if (title != null && !title.isBlank() && cleanText.startsWith(title)) {
            return cleanText.substring(title.length()).trim();
        }
        return cleanText.trim();
    }

    private List<String> splitSentences(String content) {
        if (content == null || content.isBlank()) {
            return List.of();
        }
        return Arrays.stream(content.replaceAll("\\s+", " ").split("(?<=[。！？!?])"))
                .map(String::trim)
                .filter(sentence -> !sentence.isBlank())
                .toList();
    }

    private String trimToLength(String value, int maxLength) {
        if (value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }
}
