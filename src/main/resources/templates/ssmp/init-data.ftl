<#if config.database == "MYSQL">
    <#list roles as role>
        insert into role(name, description) values('${role.name}', '${role.description}');
    </#list>
    <#list menus as menu>
        insert into menu(id, name, icon, href, role) values(${menu.id}, '${menu.name}', '${menu.icon}', '${menu.href}', '${menu.role}');
    </#list>

    <#list permissions as permission>
        insert into permission(operation, role, model) values('${permission.first}', '${permission.second}', '${permission.third}');
    </#list>

    <#list roles as role>
        insert into user(username, password, role) values('${role.name}', '${role.name}', '${role.name}');
    </#list>
<#elseif config.database == "SQLSERVER">
    <#list roles as role>
        insert into role(name, description) values('${role.name}', '${role.description}');
    </#list>
    <#list menus as menu>
        insert into menu(name, icon, href, role) values('${menu.name}', '${menu.icon}', '${menu.href}', '${menu.role}');
    </#list>

    <#list permissions as permission>
        insert into permission(operation, role, model) values('${permission.first}', '${permission.second}', '${permission.third}');
    </#list>

    <#list roles as role>
        insert into [user](username, password, role) values('${role.name}', '${role.name}', '${role.name}');
    </#list>
</#if>
