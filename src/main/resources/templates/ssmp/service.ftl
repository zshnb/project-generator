package ${packageName};

import com.baomidou.mybatisplus.extension.service.IService;
import java.util.List;
<#list dependencies as d>
import ${d}.*;
</#list>
<#assign name>${entity.name}</#assign>
public interface I${name?cap_first}Service extends IService<${name?cap_first}> {
    ${name?cap_first} add(${name?cap_first} ${name});
    ${name?cap_first} update(${name?cap_first} ${name});
    ${name?cap_first} detail(int id);
    void delete(int id);
    <#if name == "menu">
    ListResponse<MenuDto> list(String role);
    <#else>
    <#assign returnClass>
        <#if entity.table.associate>
            ${name?cap_first}Dto<#t>
        <#else>
            ${name?cap_first}<#t>
        </#if>
    </#assign>
    ListResponse<${returnClass}> page(List${name?cap_first}Request request<#if (entity.table.bindRoles?size > 0)>, User user</#if>);
    ListResponse<${name?cap_first}> listAll();
    </#if>
}
