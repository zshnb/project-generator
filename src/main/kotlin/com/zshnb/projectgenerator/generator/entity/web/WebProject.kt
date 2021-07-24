package com.zshnb.projectgenerator.generator.entity.web

data class WebProject(val config: Config,
                      val type: Int,
                      var tables: List<Table> = emptyList(),
                      var pages: List<Page> = emptyList(),
                      val roles: List<Role> = emptyList()
)

enum class WebProjectType(val code: Int) {
    LAY_UI(1),
    ELEMENT_UI(2)
    ;
}
