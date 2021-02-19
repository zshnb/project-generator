package ${packageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${entityPackageName}.${name?cap_first};
import ${dtoPackageName}.*;
import org.springframework.stereotype.Repository;

@Repository
public interface ${name?cap_first}Mapper extends BaseMapper<${name?cap_first}> {
    <#if entity.table.associate??>
        IPage<${name?cap_first}Dto> findDtos(Page<?> page);
    </#if>
}
