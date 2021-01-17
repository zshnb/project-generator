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
    ListResponse<${name?cap_first}> list(List${name?cap_first}Request request);
    </#if>
}
