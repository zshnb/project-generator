package com.github.studyPlatform.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.studyPlatform.service.ISchoolService;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;

@Service
public class SchoolServiceImpl extends ServiceImpl<SchoolMapper, School> implements ISchoolService {
    @Autowired
    private SchoolMapper schoolMapper;

    @Override
    public School add(School school) {
        save(school);
        return getById(school.getId());
    }

    @Override
    public School update(School school) {
        updateById(school);
        return getById(school.getId());
    }

    @Override
    public School detail(int id) {
        return getById(id);
    }

    @Override
    public void delete(int id) {
        getBaseMapper().deleteById(id);
    }

    @Override
    public ListResponse<School> list(ListSchoolRequest request) {
            IPage<School> page = page(new Page<>(request.getPageNumber(), request.getPageSize()));
            return new ListResponse<>(page.getRecords(), page.getTotal());
    }

}
