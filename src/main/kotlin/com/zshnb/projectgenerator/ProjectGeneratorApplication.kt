package com.zshnb.projectgenerator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [DataSourceAutoConfiguration::class])
class ProjectGeneratorApplication

fun main(args: Array<String>) {
	runApplication<ProjectGeneratorApplication>(*args)
}
