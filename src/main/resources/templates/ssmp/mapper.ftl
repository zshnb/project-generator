package ${packageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${entityPackageName}.${name?cap_first};
import org.springframework.stereotype.Repository;

@Repository
public interface ${name?cap_first}Mapper extends BaseMapper<${name?cap_first}> {

}
