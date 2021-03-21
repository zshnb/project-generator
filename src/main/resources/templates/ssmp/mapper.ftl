package ${packageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${entityPackageName}.${name?cap_first};
import ${dtoPackageName}.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ${name?cap_first}Mapper extends BaseMapper<${name?cap_first}> {
    <#if entity.table.associate>
        <#assign params>
            <#list entity.fields?filter(f -> f.column.searchable) as field>
                <#if field.column.associate??>
                    <#assign paramName>${field.column.associate.targetTableName}${field.column.associate.targetColumnName?cap_first}</#assign>
                    @Param("${paramName}") ${field.type} ${paramName}
                <#else>
                    @Param("${field.column.name}") ${field.type} ${field.column.name}
                </#if>
                <#if field_has_next>,</#if>
            </#list>
        </#assign>
        IPage<${name?cap_first}Dto> findDtos(Page<?> page, ${params});
    </#if>
}
