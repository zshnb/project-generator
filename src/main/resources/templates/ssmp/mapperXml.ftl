<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.${name?capFirst}Mapper">
    <#if entity.table.associates??>
        <select id="findDtos" resultType="${dtoPackageName}.${name?capFirst}Dto">
            select
                ${entity.table.name}.*,
            <#list entity.table.associates as associate>
                <#list associate.associateResultColumns as column>
                    ${associate.targetTableName}.${column.originColumnName} ${column.aliasColumnName}
                    <#if column_has_next>,</#if>
                </#list>
                <#if associate_has_next>,</#if>
            </#list>
            from ${entity.table.name}
            <#list entity.table.associates as associate>
                inner join ${associate.targetTableName} on ${associate.targetTableName}.${associate.targetColumnName} = ${entity.table.name}.${associate.sourceColumnName}
            </#list>
        </select>
    </#if>
</mapper>
