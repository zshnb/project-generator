spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${jdbcHost}:${jdbcPort?c}/${jdbcDatabase}?useUnicode=true&characterEncoding=utf8&serverTimezone=GMT%2B8&useSSL=false
    username: ${jdbcUser}
    password: ${jdbcPassword}

  jackson:
    date-format: yyyy-MM-dd HH:mm:ss
  mvc:
    static-path-pattern: /**
  resources:
    static-locations: classpath:/templates/,classpath:/META-INF/resources/,classpath:/resources/,classpath:/static/,classpath:/public/

mybatis-plus:
  configuration:
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl
  mapper-locations: classpath*:/xml/**/*.xml