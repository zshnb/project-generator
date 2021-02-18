package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface ICollegeService extends IService<College> {
    College add(College college);
    College update(College college);
    College detail(int id);
    void delete(int id);
    ListResponse<College> list(ListCollegeRequest request);
}
