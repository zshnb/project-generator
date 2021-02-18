package com.github.studyPlatform.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.studyPlatform.service.ICourseAssistantService;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;

@Service
public class CourseAssistantServiceImpl extends ServiceImpl<CourseAssistantMapper, CourseAssistant> implements ICourseAssistantService {
    @Autowired
    private CourseAssistantMapper courseAssistantMapper;

    @Override
    public CourseAssistant add(CourseAssistant courseAssistant) {
        save(courseAssistant);
        return getById(courseAssistant.getId());
    }

    @Override
    public CourseAssistant update(CourseAssistant courseAssistant) {
        updateById(courseAssistant);
        return getById(courseAssistant.getId());
    }

    @Override
    public CourseAssistant detail(int id) {
        return getById(id);
    }

    @Override
    public void delete(int id) {
        getBaseMapper().deleteById(id);
    }

    @Override
    public ListResponse<CourseAssistant> list(ListCourseAssistantRequest request) {
            IPage<CourseAssistant> page = page(new Page<>(request.getPageNumber(), request.getPageSize()));
            return new ListResponse<>(page.getRecords(), page.getTotal());
    }

}
