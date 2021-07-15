package ${packageName};
<#assign name>${entity.name?cap_first}</#assign>
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ${entityPackageName}.${name};
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
public interface ${name}Mapper extends BaseMapper<${name}> {
    <#if entity.table.associate>
        IPage<${name}Dto> findDtos(Page<?> page<#if entity.table.searchable>, @Param("request")List${name}Request request</#if><#if (entity.table.bindRoles?size > 0)>, @Param("user")User user</#if>);
    </#if>
    <#if config.database == "SQLSERVER">
        int count(<#if entity.table.searchable>@Param("request")List${name}Request request</#if><#if (entity.table.bindRoles?size > 0)>, @Param("user")User user</#if>);
    </#if>
}
