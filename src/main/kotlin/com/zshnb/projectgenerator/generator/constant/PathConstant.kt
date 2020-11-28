package com.zshnb.projectgenerator.generator.constant

import com.zshnb.projectgenerator.web.config.ProjectConfig
import org.springframework.stereotype.Component

@Component
class PathConstant(private val projectConfig: ProjectConfig) {
    fun srcDirPath(): String = "${projectConfig.tempDir}/src/main/java"

    fun resourcesDirPath(): String = "${projectConfig.tempDir}/src/main/resources"

    fun layUIPageDirPath(): String = "${projectConfig.tempDir}/src/main/resources/templates/page"

    fun layUIStaticDirPath(): String = "${projectConfig.tempDir}/src/main/resources/static"
}