package com.zshnb.projectgenerator

import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication
@MapperScan("com.zshnb.projectgenerator.web.mapper")
class ProjectGeneratorApplication

fun main(args: Array<String>) {
	runApplication<ProjectGeneratorApplication>(*args)
}
