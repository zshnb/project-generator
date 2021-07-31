<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.springframework.org/schema/mvc https://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <mvc:default-servlet-handler/>
    <mvc:annotation-driven/>
    <context:component-scan base-package="${rootPackageName}"/>

    <bean id="multipartResolver" class="org.springframework.web.multipart.support.StandardServletMultipartResolver"/>

    <bean id="internalResolver" class="org.springframework.web.servlet.view.InternalResourceViewResolver">
        <property name="prefix" value="/WEB-INF/jsp/"/>
        <property name="suffix" value=".jsp"/>
    </bean>

    <!-- H2 Data Source Config -->
    <bean id="dataSource" class="com.zaxxer.hikari.HikariDataSource" destroy-method="close">
        <#if database == "MYSQL">
            <property name="driverClassName" value="com.mysql.cj.jdbc.Driver"/>
            <property name="jdbcUrl" value="jdbc:mysql://${jdbcHost}:${jdbcPort?c}/${jdbcDatabase}?useUnicode=true&amp;characterEncoding=utf8&amp;serverTimezone=GMT%2B8&amp;useSSL=false"/>
        <#else>
            <property name="driverClassName" value="com.microsoft.sqlserver.jdbc.SQLServerDriver"/>
            <property name="jdbcUrl" value="jdbc:sqlserver://${jdbcHost}:${jdbcPort?c};Database=${jdbcDatabase}"/>
        </#if>
        <property name="username" value="${jdbcUser}"/>
        <property name="password" value="${jdbcPassword}"/>
    </bean>

    <!-- SqlSessionFactory Config -->
    <bean id="sqlSessionFactory" class="com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean">
        <property name="dataSource" ref="dataSource"/>
        <property name="mapperLocations" value="classpath:xml/*.xml"/>
        <property name="plugins">
            <array>
                <ref bean="mybatisPlusInterceptor"/>
            </array>
        </property>
    </bean>

    <bean id="mybatisPlusInterceptor" class="com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor">
        <property name="interceptors">
            <list>
                <ref bean="paginationInnerInterceptor"/>
            </list>
        </property>
    </bean>

    <bean id="paginationInnerInterceptor"
          class="com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor">
        <#if database == "MYSQL">
        <constructor-arg name="dbType" value="MYSQL"/>
        <#elseif config.database == "SQLSERVER">
        <constructor-arg name="dbType" value="SQL_SERVER"/>
        </#if>
    </bean>

    <bean id="countSqlParser"
          class="com.baomidou.mybatisplus.extension.plugins.pagination.optimize.JsqlParserCountOptimize">
        <property name="optimizeJoin" value="true"/>
    </bean>

    <!-- MyBatis Mapper Scan Config  -->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <property name="basePackage" value="${rootPackageName}.${mapperPackageName}"/>
    </bean>

</beans>