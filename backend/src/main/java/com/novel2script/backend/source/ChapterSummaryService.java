package com.novel2script.backend.source;

import com.novel2script.backend.project.ProjectService;
import com.novel2script.backend.source.dto.ChapterResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChapterSummaryService {

    private final ProjectService projectService;

    private final SourceChapterMapper sourceChapterMapper;

    private final ChapterSummaryGenerator chapterSummaryGenerator;

    public ChapterSummaryService(
            ProjectService projectService,
            SourceChapterMapper sourceChapterMapper,
            ChapterSummaryGenerator chapterSummaryGenerator
    ) {
        this.projectService = projectService;
        this.sourceChapterMapper = sourceChapterMapper;
        this.chapterSummaryGenerator = chapterSummaryGenerator;
    }

    @Transactional
    public List<ChapterResponse> summarizeChapters(String projectId) {
        projectService.getProjectEntity(projectId);
        List<SourceChapter> chapters = sourceChapterMapper.findByProjectIdOrderByChapterNoAsc(projectId);
        if (chapters.isEmpty()) {
            throw new IllegalArgumentException("请先提交小说文本并完成章节切分");
        }

        for (SourceChapter chapter : chapters) {
            String summary = chapterSummaryGenerator.generate(chapter);
            sourceChapterMapper.updateSummary(chapter.getId(), summary);
        }

        return sourceChapterMapper.findByProjectIdOrderByChapterNoAsc(projectId).stream()
                .map(ChapterResponse::from)
                .toList();
    }
}
