package com.github.studyPlatform.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.studyPlatform.service.IChappterService;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;

@Service
public class ChappterServiceImpl extends ServiceImpl<ChappterMapper, Chappter> implements IChappterService {
    @Autowired
    private ChappterMapper chappterMapper;

    @Override
    public Chappter add(Chappter chappter) {
        save(chappter);
        return getById(chappter.getId());
    }

    @Override
    public Chappter update(Chappter chappter) {
        updateById(chappter);
        return getById(chappter.getId());
    }

    @Override
    public Chappter detail(int id) {
        return getById(id);
    }

    @Override
    public void delete(int id) {
        getBaseMapper().deleteById(id);
    }

    @Override
    public ListResponse<Chappter> list(ListChappterRequest request) {
            IPage<Chappter> page = page(new Page<>(request.getPageNumber(), request.getPageSize()));
            return new ListResponse<>(page.getRecords(), page.getTotal());
    }

}
