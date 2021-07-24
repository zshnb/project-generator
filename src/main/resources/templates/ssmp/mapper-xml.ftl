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
<#function literalize(str)>
    <#if config.database == "MYSQL">
        <#return '`${str}`'>
    <#elseIf config.database == "SQLSERVER">
        <#return '[${str}]'>
    </#if>
</#function>
<#assign name>${entity.name?capFirst}</#assign>
<#assign tableName>${entity.table.name}</#assign>
<mapper namespace="${packageName}.${name}Mapper">
    <#if entity.table.associate>
        <select id="findDtos" resultType="${dtoPackageName + "." + name}Dto">
            select
                ${literalize(tableName)}.* <#if (entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0)?size > 0)>,</#if>
            <#list entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                <#list column.associate.associateResultColumns as associateColumn>
                    <#assign aliasColumnName = "${literalize(column.associate.targetTableName + associateColumn.originColumnName?capFirst)}">
                    ${literalize(column.associate.targetTableName)}.${literalize(associateColumn.originColumnName)} as ${aliasColumnName}<#if associateColumn_has_next>,</#if>
                </#list>
                <#if column_has_next>,</#if>
            </#list>
            from ${literalize(tableName)}
            <#list entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                inner join ${literalize(column.associate.targetTableName)} on ${literalize(column.associate.targetTableName)}.${literalize(column.associate.targetColumnName)} = ${literalize(tableName)}.${literalize(column.name)}
            </#list>
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
                    <#assign paramField>${camelize(field.column.name)}</#assign>
                    <if test="request.${paramField} != null<#if defaultValue != 'null'> and request.${paramField} != ${defaultValue}</#if>">
                        <#if field.column.associate??>
                            and ${literalize(field.column.associate.targetTableName)}.${literalize(field.column.associate.targetColumnName)} = ${r'#{request.' + paramField + '}'}
                        <#else>
                            and ${literalize(tableName)}.${literalize(field.column.name)} = ${r'#{request.' + field.name + '}'}
                        </#if>
                    </if>
                </#list>
            </where>
            <#if (entity.table.bindRoles?size > 0)>
                <#assign roles>
                    <#list entity.table.bindRoles as role>
                        '${role}'<#if role_has_next>,</#if><#t>
                    </#list>
                </#assign>
                <if test="user.role in ${r"{" + roles + "}"}">
                    and ${literalize(tableName)}.user_id = ${r"#{user.id}"}
                </if>
            </#if>
            <#if config.database == "SQLSERVER">
                order by ${literalize(tableName)}.id
            </#if>
        </select>
    </#if>

    <#if config.database == "SQLSERVER">
        <select id="count" resultType="int">
            select count(*)
            from ${literalize(tableName)}
            <#list entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                inner join ${literalize(column.associate.targetTableName)} on ${literalize(column.associate.targetTableName)}.${literalize(column.associate.targetColumnName)} = ${literalize(tableName)}.${literalize(column.name)}
            </#list>
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
                    <#assign paramField>${camelize(field.column.name)}</#assign>
                    <if test="request.${paramField} != null<#if defaultValue != 'null'> and request.${paramField} != ${defaultValue}</#if>">
                        <#if field.column.associate??>
                            and ${literalize(field.column.associate.targetTableName)}.${literalize(field.column.associate.targetColumnName)} = ${r'#{request.' + paramField + '}'}
                        <#else>
                            and ${literalize(tableName)}.${literalize(field.column.name)} = ${r'#{request.' + field.name + '}'}
                        </#if>
                    </if>
                </#list>
            </where>
            <#if (entity.table.bindRoles?size > 0)>
                <#assign roles>
                    <#list entity.table.bindRoles as role>
                        '${role}'<#if role_has_next>,</#if><#t>
                    </#list>
                </#assign>
                <if test="user.role in ${r"{" + roles + "}"}">
                    and ${literalize(tableName)}.user_id = ${r"#{user.id}"}
                </if>
            </#if>
        </select>
    </#if>
</mapper>
