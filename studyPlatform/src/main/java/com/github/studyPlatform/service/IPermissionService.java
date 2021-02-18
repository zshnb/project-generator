package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface IPermissionService extends IService<Permission> {
    Permission add(Permission permission);
    Permission update(Permission permission);
    Permission detail(int id);
    void delete(int id);
    ListResponse<Permission> list(ListPermissionRequest request);
}
