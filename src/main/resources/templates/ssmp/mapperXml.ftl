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
                    <#if entity.table.columns[column_index + 1]?? && entity.table.columns[column_index + 1].associate??>,</#if>
                </#if>
            </#list>
            from ${entity.table.name}
            <#list entity.table.columns as column>
                <#if column.associate??>
                    inner join ${column.associate.targetTableName} on ${column.associate.targetTableName}.${column.associate.targetColumnName} = ${entity.table.name}.${column.associate.sourceColumnName}
                </#if>
            </#list>
            <#if entity.table.searchable>
                <where>
                    <#list entity.fields?filter(f -> f.column.searchable) as field>
                        <#switch field.type>
                            <#case "String">
                                <#assign defaultValue>''</#assign>
                            <#case "Integer">
                            <#assign defaultValue>0</#assign>
                        </#switch>
                        <#if field.column.associate??>
                            <#assign associateFieldParam>
                                ${field.column.associate.targetTableName}${field.column.associate.targetColumnName?capFirst}
                            </#assign>
                            <if test="<#compress>${associateFieldParam}</#compress> != ${defaultValue}">
                                and ${field.column.associate.targetTableName}.${field.column.associate.targetColumnName} = ${r'#{'}<#compress>${associateFieldParam}</#compress>${r'}'}
                            </if>
                        <#else>
                            <if test="<#compress>${field.column.name}</#compress> != ${defaultValue}">
                                and ${field.column.name} = ${r'#{'}${field.column.name}${r'}'}
                            </if>
                        </#if>
                    </#list>
                </where>
            </#if>
        </select>
    </#if>
</mapper>
