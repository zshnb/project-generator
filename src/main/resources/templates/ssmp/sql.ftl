<#list tables as table>
create table `${table.name}` (
<#list  table.columns as column>
    `${column.name}` ${column.type}<#if column.length &gt; 0>(${column.length})</#if><#if column.primary> AUTO_INCREMENT PRIMARY KEY</#if><#if column_has_next>,</#if>
</#list>
);

</#list>


