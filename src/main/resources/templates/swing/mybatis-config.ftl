<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <settings>
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <#if config.database == "MYSQL">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://${config.jdbcHost}:${config.jdbcPort?c}/${config.jdbcDatabase}?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=GMT%2B8&amp;useSSL=false"/>
                <property name="username" value="${config.jdbcUser}"/>
                <property name="password" value="${config.jdbcPassword}"/>
                <#elseIf config.database == "SQLSERVER">
                <property name="driver" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
                <property name="url" value="jdbc:sqlserver://${config.jdbcHost}:${config.jdbcPort?c};Database=${config.jdbcDatabase}"/>
                <property name="username" value="${config.jdbcUser}"/>
                <property name="password" value="${config.jdbcPassword}"/>
                </#if>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <#list entities as entity>
            <mapper resource="xml/${entity.name?capFirst}Mapper.xml"/>
        </#list>
    </mappers>
</configuration>