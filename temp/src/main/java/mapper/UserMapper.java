package .mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import .entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

}
