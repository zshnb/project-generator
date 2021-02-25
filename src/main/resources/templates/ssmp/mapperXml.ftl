<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.${name?capFirst}Mapper">
    <#if entity.table.associate>
        <select id="findDtos" resultType="${dtoPackageName}.${name?capFirst}Dto">
            select
                ${entity.table.name}.*,
            <#list entity.table.columns as column>
                <#if column.associate??>
                    <#list column.associate.associateResultColumns as associateColumn>
                        ${column.associate.targetTableName}.${associateColumn.originColumnName} as ${associateColumn.aliasColumnName}
                        <#if associateColumn_has_next>,</#if>
                    </#list>
                </#if>
                <#if entity.table.columns[column_index + 1]?? && entity.table.columns[column_index + 1].associate??>,</#if>
            </#list>
            from ${entity.table.name}
            <#list entity.table.columns as column>
                <#if column.associate??>
                    inner join ${column.associate.targetTableName} on ${column.associate.targetTableName}.${column.associate.targetColumnName} = ${entity.table.name}.${column.associate.sourceColumnName}
                </#if>
            </#list>
        </select>
    </#if>
</mapper>
