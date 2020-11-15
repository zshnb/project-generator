package com.zshnb.projectgenerator

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ProjectGeneratorApplication

fun main(args: Array<String>) {
	runApplication<ProjectGeneratorApplication>(*args)
}
