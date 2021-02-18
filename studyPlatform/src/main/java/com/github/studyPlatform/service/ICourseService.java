package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface ICourseService extends IService<Course> {
    Course add(Course course);
    Course update(Course course);
    Course detail(int id);
    void delete(int id);
    ListResponse<Course> list(ListCourseRequest request);
}
