package ${packageName};
<#assign name>${entity.name?cap_first}</#assign>
import ${entityPackageName}.*;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.lang.String;

public interface ${name}Mapper {
    ${name} selectById(int id);
    <#if name == "Menu">
    List<Menu> list(String role);
    <#elseif name == "Permission">
    List<Permission> list(@Param("role") String role, @Param("model") String model);
    <#elseif name == "User">
    User selectByUserNameAndPasswordAndRole(@Param("userName")String userName, @Param("password")String password, @Param("role")String role);
    <#else>
    </#if>
    List<${name}> list();
    void insert(${name} ${name?uncap_first});
    void update(${name} ${name?uncap_first});
    void deleteById(int id);
    <#if entity.table.associate>
    List<${name}Dto> findDtos(<#if entity.table.searchable>@Param("request")List${name}Request request</#if><#if (entity.table.bindRoles?size > 0)>, @Param("user")User user</#if>);
    </#if>
}
