package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface ICourseAssistantService extends IService<CourseAssistant> {
    CourseAssistant add(CourseAssistant courseAssistant);
    CourseAssistant update(CourseAssistant courseAssistant);
    CourseAssistant detail(int id);
    void delete(int id);
    ListResponse<CourseAssistant> list(ListCourseAssistantRequest request);
}
