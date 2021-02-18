package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface IUserService extends IService<User> {
    User add(User user);
    User update(User user);
    User detail(int id);
    void delete(int id);
    ListResponse<User> list(ListUserRequest request);
}
