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
        from ${entity.name}
        where ${primaryKey} = ${r"#{" + primaryKey + "}"}
    </select>

    <insert id="insert" parameterType="${entityPackageName}.${name}">
        <#assign parameters>
            <#list entity.fields as field>
                ${r"#{" + field.name + "}"}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        insert into ${camelize(tableName)} values(${parameters})
    </insert>

    <update id="update" parameterType="${entityPackageName}.${name}">
        <#assign parameters>
            <#list entity.fields as field>
                ${field.column.name} = ${r"#{" + field.name + "}"}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>
        update ${camelize(tableName)} set ${parameters} where ${primaryKey} = ${r"#{" + primaryKey + "}"}
    </update>

    <select id="list" resultType="${entityPackageName}.${name}">
        select *
        from ${entity.name}
    </select>

    <delete id="deleteById">
        delete from ${camelize(tableName)} where ${primaryKey} = ${r"#{" + primaryKey + "}"}
    </delete>
</mapper>
