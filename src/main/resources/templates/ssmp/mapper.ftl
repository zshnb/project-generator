package ${packageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${entityPackageName}.${name?cap_first};
import ${dtoPackageName}.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
<#function camelize(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncap_first>
</#function>
@Repository
public interface ${name?cap_first}Mapper extends BaseMapper<${name?cap_first}> {
    <#if entity.table.associate>
        <#assign params>
            <#list entity.fields?filter(f -> f.column.searchable) as field>
                <#if field.column.associate??>
                    <#assign paramName>${camelize(field.column.associate.targetTableName)}${camelize(field.column.associate.targetColumnName)?cap_first}</#assign>
                    @Param("${paramName}") ${field.type} ${paramName}
                <#else>
                    @Param("${field.column.name}") ${field.type} ${field.column.name}
                </#if>
                <#if field_has_next>,</#if>
            </#list>
        </#assign>
        IPage<${name?cap_first}Dto> findDtos(Page<?> page<#if params != "">, </#if>${params});
    </#if>
}
