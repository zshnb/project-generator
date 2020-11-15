package .service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import .entity.*;
import .common.*;
import .request.*;
import .mapper.*;
import .dto.*;

public interface IMenuService extends IService<Menu> {
    Menu add(Menu menu);
    Menu update(Menu menu);
    Menu detail(int id);
    void delete(int id);
    ListResponse<MenuDto> list(String role);
}
