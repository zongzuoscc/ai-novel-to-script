package com.novel2script.backend.source;

/**
 * 章节摘要生成器。当前默认实现为本地规则版，后续可替换为 AI 调用实现。
 */
public interface ChapterSummaryGenerator {

    String generate(SourceChapter chapter);
}
