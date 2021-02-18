package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface ISchoolService extends IService<School> {
    School add(School school);
    School update(School school);
    School detail(int id);
    void delete(int id);
    ListResponse<School> list(ListSchoolRequest request);
}
