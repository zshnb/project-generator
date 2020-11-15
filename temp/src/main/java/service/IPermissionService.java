package .service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import .entity.*;
import .common.*;
import .request.*;
import .mapper.*;
import .dto.*;

public interface IPermissionService extends IService<Permission> {
    Permission add(Permission permission);
    Permission update(Permission permission);
    Permission detail(int id);
    void delete(int id);
    ListResponse<Permission> list(ListPermissionRequest request);
}
