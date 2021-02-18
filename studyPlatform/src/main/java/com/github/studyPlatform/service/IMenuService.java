package com.github.studyPlatform.service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import com.github.studyPlatform.entity.*;
import com.github.studyPlatform.common.*;
import com.github.studyPlatform.request.*;
import com.github.studyPlatform.mapper.*;
import com.github.studyPlatform.dto.*;
public interface IMenuService extends IService<Menu> {
    Menu add(Menu menu);
    Menu update(Menu menu);
    Menu detail(int id);
    void delete(int id);
    ListResponse<MenuDto> list(String role);
}
