package com.zshnb.projectgenerator.generator.constant

import com.zshnb.projectgenerator.generator.entity.Config
import com.zshnb.projectgenerator.web.config.ProjectConfig
import org.springframework.stereotype.Component

class PathConstant {
    companion object {
        fun javaSrcDirPath(config: Config): String = "${config.artifactId}/src/main/java"

        fun resourcesDirPath(config: Config): String = "${config.artifactId}/src/main/resources"

        fun layUIPageDirPath(config: Config): String = "${config.artifactId}/src/main/resources/templates/page"

        fun layUIStaticDirPath(config: Config): String = "${config.artifactId}/src/main/resources/static"
    }
}