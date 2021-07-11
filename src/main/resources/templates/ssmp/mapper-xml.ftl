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
<#function literalize(tableName)>
    <#if config.database == "MYSQL">
        <#return '`${tableName}`'>
    <#elseIf config.database == "SQLSERVER">
        <#return '[${tableName}]'>
    </#if>
</#function>
<mapper namespace="${mapper.packageName}.${mapper.name?capFirst}Mapper">
    <#if mapper.entity.table.associate>
        <select id="findDtos" resultType="${mapper.dtoPackageName}.${mapper.name?capFirst}Dto">
            select
                ${literalize(mapper.entity.table.name)}.* <#if (mapper.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0)?size > 0)>,</#if>
            <#list mapper.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                <#list column.associate.associateResultColumns as associateColumn>
                    ${literalize(column.associate.targetTableName)}.${literalize(associateColumn.originColumnName)} as ${associateColumn.aliasColumnName}<#if associateColumn_has_next>,</#if>
                </#list>
                <#if column_has_next>,</#if>
            </#list>
            from ${literalize(mapper.entity.table.name)}
            <#list mapper.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                inner join ${literalize(column.associate.targetTableName)} on ${literalize(column.associate.targetTableName)}.${literalize(column.associate.targetColumnName)} = ${literalize(mapper.entity.table.name)}.${literalize(column.associate.sourceColumnName)}
            </#list>
            <#if mapper.entity.table.searchable>
                <where>
                    <#list mapper.entity.fields?filter(f -> f.column.searchable) as field>
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
                                ${camelize(field.column.associate.sourceColumnName)}
                            </#assign>
                            <if test="request.<#compress>${associateFieldParam}</#compress> != null and request.<#compress>${associateFieldParam}</#compress> != ${defaultValue}">
                                and ${literalize(field.column.associate.targetTableName)}.${literalize(field.column.associate.targetColumnName)} = ${r'#{request.'}<#compress>${associateFieldParam}</#compress>${r'}'}
                            </if>
                        <#else>
                            <if test="request.${field.column.name} != null and request.${field.column.name} != ${defaultValue}">
                                and ${literalize(field.column.name)} = ${r'#{request.'}${field.name}${r'}'}
                            </if>
                        </#if>
                    </#list>
                </where>
                <#if (mapper.entity.table.bindRoles?size > 0)>
                    <#list mapper.entity.table.bindRoles as role>
                        <if test="user.role == '${role}'">
                            and ${literalize(mapper.entity.table.name)}.user_id = ${r"#{user.id}"}
                        </if>
                    </#list>
                </#if>
            <#else>
                <#if (mapper.entity.table.bindRoles?size > 0)>
                    <#list mapper.entity.table.bindRoles as role>
                        <if test="user.role == '${role}'">
                            where ${literalize(mapper.entity.table.name)}.user_id = ${r"#{user.id}"}
                        </if>
                    </#list>
                </#if>
            </#if>
            <#if config.database == "SQLSERVER">
                order by ${literalize(mapper.entity.table.name)}.id
            </#if>
        </select>
    </#if>

    <#if config.database == "SQLSERVER">
        <select id="count" resultType="int">
            select count(*)
            from ${literalize(mapper.entity.table.name)}
            <#list mapper.entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                inner join ${literalize(column.associate.targetTableName)} on ${literalize(column.associate.targetTableName)}.${literalize(column.associate.targetColumnName)} = ${literalize(mapper.entity.table.name)}.${literalize(column.associate.sourceColumnName)}
            </#list>
            <#if mapper.entity.table.searchable>
                <where>
                    <#list mapper.entity.fields?filter(f -> f.column.searchable) as field>
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
                                ${camelize(field.column.associate.sourceColumnName)}
                            </#assign>
                            <if test="request.<#compress>${associateFieldParam}</#compress> != null and request.<#compress>${associateFieldParam}</#compress> != ${defaultValue}">
                                and ${literalize(field.column.associate.targetTableName)}.${literalize(field.column.associate.targetColumnName)} = ${r'#{request.'}<#compress>${associateFieldParam}</#compress>${r'}'}
                            </if>
                        <#else>
                            <if test="request.${field.column.name} != null and request.${field.column.name} != ${defaultValue}">
                                and ${literalize(field.column.name)} = ${r'#{request.'}${field.name}${r'}'}
                            </if>
                        </#if>
                    </#list>
                </where>
                <#if (mapper.entity.table.bindRoles?size > 0)>
                    <#list mapper.entity.table.bindRoles as role>
                        <if test="user.role == '${role}'">
                            and ${literalize(mapper.entity.table.name)}.user_id = ${r"#{user.id}"}
                        </if>
                    </#list>
                </#if>
            <#else>
                <#if (mapper.entity.table.bindRoles?size > 0)>
                    <#list mapper.entity.table.bindRoles as role>
                        <if test="user.role == '${role}'">
                            where ${literalize(mapper.entity.table.name)}.user_id = ${r"#{user.id}"}
                        </if>
                    </#list>
                </#if>
            </#if>
        </select>
    </#if>
</mapper>
