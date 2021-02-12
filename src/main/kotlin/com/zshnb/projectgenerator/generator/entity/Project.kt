package com.zshnb.projectgenerator.generator.entity

import com.zshnb.projectgenerator.generator.entity.ProjectType.LAY_UI

data class Project(val config: Config = Config(),
                   val type: Int = LAY_UI.code,
                   var tables: List<Table> = emptyList(),
                   var controllers: List<Controller> = emptyList(),
                   var entities: List<Entity> = emptyList(),
                   var services: List<Service> = emptyList(),
                   var mappers: List<Mapper> = emptyList(),
                   var pages: List<Page> = emptyList(),
                   val roles: List<Role> = emptyList()
)

enum class ProjectType(val code: Int) {
    LAY_UI(1),
    ELEMENT_UI(2)
    ;

    companion object {
        fun fromCode(code: Int): ProjectType =
            when (code) {
                1 -> LAY_UI
                2 -> ELEMENT_UI
                else -> LAY_UI
            }
    }
}