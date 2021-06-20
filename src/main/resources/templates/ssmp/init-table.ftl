<#list tables as table>
create table `${table.name}` (
<#list  table.columns as column>
    <#switch column.type>
        <#case "TEXT">
        <#case "VARCHAR">
            <#assign defaultValue>''</#assign>
            <#break>
        <#case "INT">
            <#assign defaultValue>0</#assign>
            <#break>
    </#switch>
    `${column.name}` ${column.type.description}<#if column.length != "0">(${column.length})</#if><#if !column.nullable> DEFAULT ${defaultValue}</#if><#if column.primary> AUTO_INCREMENT PRIMARY KEY</#if><#if column_has_next>,</#if>
</#list>
);

</#list>


