package .service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import .entity.*;
import .common.*;
import .request.*;
import .mapper.*;
import .dto.*;

public interface IRoleService extends IService<Role> {
    Role add(Role role);
    Role update(Role role);
    Role detail(int id);
    void delete(int id);
    ListResponse<Role> list(ListRoleRequest request);
}
