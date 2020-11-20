package com.zshnb.projectgenerator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.test.context.SpringBootTest

class ProjectGeneratorTestApplication

fun main(args: Array<String>) {
	runApplication<ProjectGeneratorTestApplication>(*args)
}
