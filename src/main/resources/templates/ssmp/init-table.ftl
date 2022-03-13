<#function literalize(str)>
    <#if config.database == "MYSQL">
        <#return '`${str}`'>
    <#elseif config.database == "SQLSERVER">
        <#return '[${str}]'>
    </#if>
</#function>
<#assign autoIncrementStatement>
    <#if config.database == "MYSQL">
        AUTO_INCREMENT<#t>
    <#elseif config.database == "SQLSERVER">
        IDENTITY(1,1)<#t>
    </#if>
</#assign>
<#list tables as table>
create table ${literalize(table.name)} (
<#list table.columns as column>
    <#switch column.type>
        <#case "TEXT">
        <#case "VARCHAR">
            <#assign defaultValue>''</#assign>
            <#break>
        <#case "INT">
            <#assign defaultValue>0</#assign>
            <#break>
    </#switch>
    ${literalize(column.name)} ${column.type.description}<#rt>
    <#if column.length != "0">(${column.length})</#if><#t>
    <#if !column.nullable> DEFAULT ${defaultValue}</#if><#t>
    <#if column.primary> ${autoIncrementStatement} PRIMARY KEY</#if><#t>
    <#if config.database == "MYSQL"> COMMENT '${column.comment}'</#if><#t>
    <#if column_has_next>,<#t></#if>
</#list>
)<#if config.database == "MYSQL">COMMENT='${table.comment}'</#if>;

</#list>


