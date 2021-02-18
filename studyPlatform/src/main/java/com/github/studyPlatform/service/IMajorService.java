package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface IMajorService extends IService<Major> {
    Major add(Major major);
    Major update(Major major);
    Major detail(int id);
    void delete(int id);
    ListResponse<Major> list(ListMajorRequest request);
}
