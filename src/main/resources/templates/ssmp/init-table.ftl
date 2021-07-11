<#assign autoIncrementStatement>
    <#if config.database == "MYSQL">
        AUTO_INCREMENT
    <#elseif config.database == "SQLSERVER">
        IDENTITY(1,1)
    </#if>
</#assign>
<#list tables as table>
    <#assign tableName>
        <#if config.database == "MYSQL">
            `${table.name}`
        <#elseif config.database == "SQLSERVER">
            [${table.name}]
        </#if>
    </#assign>
create table <#compress>${tableName}</#compress> (
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
    <#assign columnName>
        <#if config.database == "MYSQL">
            `${column.name}`
        <#elseif config.database == "SQLSERVER">
            [${column.name}]
        </#if>
    </#assign>
        <#compress>${columnName}</#compress> ${column.type.description}<#rt>
        <#if column.length != "0"><#compress>(${column.length})</#compress></#if><#t>
        <#if !column.nullable><#compress>DEFAULT ${defaultValue}</#compress></#if><#t>
        <#if column.primary><#compress>${autoIncrementStatement}</#compress> PRIMARY KEY </#if><#t>
        <#if config.database == "MYSQL"><#compress>COMMENT '${column.comment}'</#compress></#if><#t>
        <#if column_has_next><#compress>,</#compress></#if><#t>

</#list>
)<#if config.database == "MYSQL">COMMENT='${table.comment}'</#if>;

</#list>


