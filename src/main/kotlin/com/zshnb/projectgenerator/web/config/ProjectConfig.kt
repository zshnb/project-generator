package com.zshnb.projectgenerator.web.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "spring")
class ProjectConfig {
    var tempDir: String = ""
}