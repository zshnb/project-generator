<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${packageName}.${name?capFirst}Mapper">
    ${entity.table.associate.targetTableName?capFirst}
    <#if entity.table.associate.targetTableName?hasContent>
        <select id="findDtos" resultType="#{dtoPackageName}.#{name?capFirst}Dto">
            select
                #{entity.table.name}.*,
            <#list entity.table.associate.associateResultColumns as column>
                #{entity.table.associate.targetTableName}.#{column.originColumnName} #{column.aliasColumnName}
                <#if column_has_next>,</#if>
            </#list>
            from #{entity.table.name}
            inner join #{entity.table.associate.targetTableName} on #{entity.table.associate.targetTableName}.#{entity.table.associate.targetColumnName} = #{entity.table.associate.sourceColumnName}
        </select>
    </#if>
</mapper>
