package ${packageName};

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${entityPackageName}.${name?cap_first};
<#if (entity.table.bindRoles?size > 0)>
import ${entityPackageName}.User;
</#if>
<#if entity.table.searchable>
    import ${requestPackageName}.*;
</#if>
import ${dtoPackageName}.*;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.time.LocalDate;
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
        IPage<${name?cap_first}Dto> findDtos(Page<?> page<#if entity.table.searchable>, @Param("request")List${name?cap_first}Request request</#if><#if (entity.table.bindRoles?size > 0)>, @Param("user")User user</#if>);
    </#if>
}
