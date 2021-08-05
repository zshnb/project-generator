package com.zshnb.projectgenerator.generator.config

import com.zshnb.projectgenerator.generator.entity.web.Config
import com.zshnb.projectgenerator.web.config.ProjectConfig
import org.springframework.stereotype.Component

@Component
class PathConfig(private val projectConfig: ProjectConfig) {
    fun javaSrcDirPath(config: Config): String =
        "${projectConfig.projectDir}/${config.artifactId}/src/main/java"

    fun resourcesDirPath(config: Config): String = "${projectConfig.projectDir}/${config.artifactId}/src/main/resources"

    fun webappDirPath(config: Config): String = "${projectConfig.projectDir}/${config.artifactId}/src/main/webapp"

    fun thymeleafPageDirPath(config: Config): String =
        "${projectConfig.projectDir}/${config.artifactId}/src/main/resources/templates/page"

    fun thymeleafTemplateDirPath(config: Config): String =
        "${projectConfig.projectDir}/${config.artifactId}/src/main/resources/templates"

    fun jspPageDir(config: Config): String = "${webappDirPath(config)}/WEB-INF/jsp"

    fun layUIStaticDirPath(config: Config): String =
        "${projectConfig.projectDir}/${config.artifactId}/src/main/resources/static"

    fun rootDir(config: Config) = "${javaSrcDirPath(config)}/${config.rootPackageName.replace('.', '/')}"
    fun entityDir(config: Config) = "${rootDir(config)}/${config.entityPackageName}"
    fun serviceDir(config: Config) = "${rootDir(config)}/${config.servicePackageName}"
    fun serviceImplDir(config: Config) = "${this.rootDir(config)}/${config.serviceImplPackageName}"
    fun mapperDir(config: Config) = "${this.rootDir(config)}/${config.mapperPackageName}"
    fun controllerDir(config: Config) = "${this.rootDir(config)}/${config.controllerPackageName}"
    fun requestDir(config: Config) = "${this.rootDir(config)}/request"
    fun commonDir(config: Config) = "${this.rootDir(config)}/common"
    fun configDir(config: Config) = "${this.rootDir(config)}/config"
    fun exceptionDir(config: Config) = "${this.rootDir(config)}/exception"
    fun dtoDir(config: Config) = "${this.rootDir(config)}/dto"
    fun xmlDir(config: Config) = "${resourcesDirPath(config)}/xml"
    fun frameDir(config: Config) = "${this.rootDir(config)}/frame"
}
