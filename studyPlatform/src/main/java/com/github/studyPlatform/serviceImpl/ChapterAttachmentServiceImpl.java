package com.github.studyPlatform.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.studyPlatform.service.IChapterAttachmentService;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;

@Service
public class ChapterAttachmentServiceImpl extends ServiceImpl<ChapterAttachmentMapper, ChapterAttachment> implements IChapterAttachmentService {
    @Autowired
    private ChapterAttachmentMapper chapterAttachmentMapper;

    @Override
    public ChapterAttachment add(ChapterAttachment chapterAttachment) {
        save(chapterAttachment);
        return getById(chapterAttachment.getId());
    }

    @Override
    public ChapterAttachment update(ChapterAttachment chapterAttachment) {
        updateById(chapterAttachment);
        return getById(chapterAttachment.getId());
    }

    @Override
    public ChapterAttachment detail(int id) {
        return getById(id);
    }

    @Override
    public void delete(int id) {
        getBaseMapper().deleteById(id);
    }

    @Override
    public ListResponse<ChapterAttachment> list(ListChapterAttachmentRequest request) {
            IPage<ChapterAttachment> page = page(new Page<>(request.getPageNumber(), request.getPageSize()));
            return new ListResponse<>(page.getRecords(), page.getTotal());
    }

}
