package com.zshnb.projectgenerator.generator.entity.web

data class WebProject(val config: Config,
                      val type: WebProjectType,
                      var tables: List<Table> = emptyList(),
                      var pages: List<Page> = emptyList(),
                      val roles: List<Role> = emptyList()
)

enum class WebProjectType {
    SSM,
    SBMP
    ;
}
