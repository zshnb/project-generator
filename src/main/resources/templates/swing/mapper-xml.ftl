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
<#assign primaryKey = "${entity.table.columns?filter(it -> it.primary)[0].name}"/>
<mapper namespace="${packageName}.${name}Mapper">
    <select id="selectById" resultType="${entityPackageName}.${name}">
        select *
        from ${literalize(tableName)}
        where ${primaryKey} = ${r"#{" + primaryKey + "}"}
    </select>
    <#if entity.name == "user">
        <select id="selectByUserNameAndPasswordAndRole" resultType="${entityPackageName}.User">
            select u.*
            from ${literalize(r"user")} u
            inner join role r on u.role = r.name
            where u.username = ${r"#{userName}"} and u.password = ${r"#{password}"} and r.description = ${r"#{role}"}
        </select>
    <#elseIf name == "Role">
        <select id="selectByDescription" resultType="${entityPackageName}.Role">
            select *
            from role
            where description = ${r"#{description}"}
        </select>
    </#if>

    <insert id="insert" parameterType="${entityPackageName}.${name}" useGeneratedKeys="true" keyProperty="id">
        <#assign columnNames>
            <#list entity.fields?filter(it -> it.column.enableFormItem) as field>
                ${field.column.name}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        <#assign parameters>
            <#list entity.fields?filter(it -> it.column.enableFormItem) as field>
                ${r"#{" + field.name + "}"}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        <#assign nowFunction>
            <#if config.database == "MYSQL">
                now()<#t>
            <#elseIf config.database == "SQLSERVER">
                getdate()<#t>
            </#if>
        </#assign>
        insert into ${literalize(tableName)}(${columnNames}, create_at, update_at) values(${parameters}, ${nowFunction}, ${nowFunction})
    </insert>

    <update id="update" parameterType="${entityPackageName}.${name}">
        <#assign columnNames>
            <#list entity.fields?filter(it -> it.column.enableFormItem) as field>
                ${field.column.name}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        <#assign parameters>
            <#list entity.fields?filter(it -> it.column.enableFormItem) as field>
                ${field.column.name} = ${r"#{" + field.name + "}"}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        update ${literalize(tableName)} set ${parameters} where ${primaryKey} = ${r"#{" + primaryKey + "}"}
    </update>

    <#if name == "Menu">
        <select id="list" resultType="${entityPackageName}.${name}">
            select *
            from menu
            where role = ${r"#{role}"}
        </select>
    <#elseIf name = "Permission">
        <select id="list" resultType="${entityPackageName}.${name}">
            select *
            from permission
            where role = ${r"#{role}"} and model = ${r"#{model}"}
        </select>
    <#else>
        <select id="list" resultType="${entityPackageName}.${name}">
            select *
            from ${literalize(tableName)}
        </select>
    </#if>

    <delete id="deleteById">
        delete from ${literalize(tableName)} where ${primaryKey} = ${r"#{" + primaryKey + "}"}
    </delete>

    <#if entity.table.associate>
        <select id="findDtos" resultType="${dtoPackageName + "." + name}Dto">
            select
            ${literalize(tableName)}.* <#if (entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0)?size > 0)>,</#if>
            <#list entity.table.columns?filter(c -> c.associate?? && c.associate.associateResultColumns?size > 0) as column>
                <#list column.associate.associateResultColumns as associateColumn>
                    <#assign camelizeColumnName = camelize(column.associate.targetTableName + "_" + associateColumn.originColumnName)/>
                    <#assign aliasColumnName = "${literalize(camelizeColumnName)}">
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
        </select>
    </#if>
</mapper>
