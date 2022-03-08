package com.zshnb.projectgenerator.generator.entity.web

data class WebProject(val config: Config,
                      val type: WebProjectType,
                      var tables: List<Table> = emptyList(),
                      val pages: List<Page> = emptyList(),
                      val roles: List<Role> = emptyList()
) {
    fun listUnbindMenus(): List<Menu> =
        roles.flatMap { it.menus }
            .filter { !it.bind }
            .distinctBy { it.name }
}

/**
 * @param split 是否为前后端分离
 */
enum class WebProjectType(val split: Boolean) {
    SSM(false),
    SBMP(false),
    SBMP_SPLIT(true),
    ;
}
