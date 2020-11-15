package .service;

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
import .entity.*;
import .common.*;
import .request.*;
import .mapper.*;
import .dto.*;

public interface IUserService extends IService<User> {
    User add(User user);
    User update(User user);
    User detail(int id);
    void delete(int id);
    ListResponse<User> list(ListUserRequest request);
}
