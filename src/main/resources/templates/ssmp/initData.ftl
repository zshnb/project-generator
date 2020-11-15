<#list roles as role>
    insert into role(name) values('${role.name}');
</#list>
<#list menus as menu>
    insert into menu(id, parent_id, name, icon, href, role) values(${menu.id}, ${menu.parentId}, '${menu.name}', '${menu.icon}', '${menu.href}', '${menu.role}');
</#list>

<#list permissions as permission>
    insert into permission(operation, role, model) values('${permission.operation}', '${permission.role}', '${permission.model}');
</#list>
