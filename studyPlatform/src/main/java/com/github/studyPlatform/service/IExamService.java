package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface IExamService extends IService<Exam> {
    Exam add(Exam exam);
    Exam update(Exam exam);
    Exam detail(int id);
    void delete(int id);
    ListResponse<Exam> list(ListExamRequest request);
}
