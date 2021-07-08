<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<#function camelize(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncapFirst>
</#function>
<mapper namespace="${packageName}.${name?capFirst}Mapper">
    <#if entity.table.associate>
        <select id="findDtos" resultType="${dtoPackageName}.${name?capFirst}Dto">
            select
                ${entity.table.name}.* <#if (entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0)?size > 0)>,</#if>
            <#list entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                <#list column.associate.associateResultColumns as associateColumn>
                    ${column.associate.targetTableName}.${associateColumn.originColumnName} as ${associateColumn.aliasColumnName}<#if associateColumn_has_next>,</#if>
                </#list>
                <#if column_has_next>,</#if>
            </#list>
            from ${entity.table.name}
            <#list entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                inner join ${column.associate.targetTableName} on ${column.associate.targetTableName}.${column.associate.targetColumnName} = ${entity.table.name}.${column.associate.sourceColumnName}
            </#list>
            <#if entity.table.searchable>
                <where>
                    <#list entity.fields?filter(f -> f.column.searchable) as field>
                        <#switch field.type>
                            <#case "String">
                                <#assign defaultValue>''</#assign>
                                <#break>
                            <#case "Integer">
                                <#assign defaultValue>0</#assign>
                                <#break>
                            <#case "LocalDate">
                            <#case "LocalDateTime">
                                <#assign defaultValue>null</#assign>
                                <#break>
                        </#switch>
                        <#if field.column.associate??>
                            <#assign associateFieldParam>
                                ${camelize(field.column.associate.targetTableName)}${camelize(field.column.associate.targetColumnName)?capFirst}
                            </#assign>
                            <if test="<#compress>${associateFieldParam}</#compress> != ${defaultValue}">
                                and ${field.column.associate.targetTableName}.${field.column.associate.targetColumnName} = ${r'#{'}<#compress>${associateFieldParam}</#compress>${r'}'}
                            </if>
                        <#else>
                            <if test="<#compress>${field.column.name}</#compress> != ${defaultValue}">
                                and ${field.column.name} = ${r'#{'}${field.name}${r'}'}
                            </if>
                        </#if>
                    </#list>
                </where>
                <#if (entity.table.bindRoles?size > 0)>
                    <#list entity.table.bindRoles as role>
                        <if test="user.role == '${role}'">
                            and ${entity.table.name}.user_id = ${r"#{user.id}"}
                        </if>
                    </#list>
                </#if>
            <#else>
                <#if (entity.table.bindRoles?size > 0)>
                    <#list entity.table.bindRoles as role>
                        <if test="user.role == '${role}'">
                            where ${entity.table.name}.user_id = ${r"#{user.id}"}
                        </if>
                    </#list>
                </#if>
            </#if>
        </select>
    </#if>
</mapper>
