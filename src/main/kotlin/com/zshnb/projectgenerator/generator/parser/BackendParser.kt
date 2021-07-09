package com.zshnb.projectgenerator.generator.parser

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.entity.ColumnType.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.util.TypeUtil
import com.zshnb.projectgenerator.generator.util.toCamelCase
import org.springframework.stereotype.Component

/**
 * 从[Project]的json中解析出项目的各个部分
 * */
@Component
class BackendParser(private val moshi: Moshi,
                    private val typeUtil: TypeUtil) {
    fun parseProject(json: String): Project {
        val adapter = moshi.adapter(Project::class.java)
        val project = adapter.fromJson(json)!!
        project.tables = project.tables + buildRoleAndMenuAndPermissionTable()
        project.tables = project.tables.map {
            val columns = (it.columns.toSet() + setOf(
                Column("id", INT, length = "11", primary = true, comment = "id主键"),
                Column("create_at", DATETIME, comment = "创建时间"),
                Column("update_at", DATETIME, comment = "更新时间")
            )).toList()
            Table(it.name, it.comment, columns, it.permissions, it.columns.any { column -> column.searchable },
                it.enablePage, it.columns.any { column -> column.associate != null }, it.bindRoles)
        }
        project.roles.forEach { role ->
            role.menus.forEach {
                it.role = role.name
            }
        }
        val entities = parseEntities(project.tables, project.config)
        val services = parseServices(entities, project.config)
        val mappers = parseMappers(entities, project.config)
        val controllers = parseController(entities, project.config)

        project.entities = entities
        project.services = services
        project.mappers = mappers
        project.controllers = controllers
        return project
    }

    /**
     * 从[Table]解析出Java代码的实体类
     * */
    private fun parseEntities(tables: List<Table>, config: Config): List<Entity> {
        return tables.map {
            val fields = it.columns.map { column ->
                Field(column.name.toCamelCase(), column, typeUtil.convertColumnTypeToFieldType(column.type).description)
            }
            Entity(config.entityPackagePath(), it.name.toCamelCase(), it, fields)
        }
    }

    /**
     * 创建角色，菜单，权限表
     * */
    private fun buildRoleAndMenuAndPermissionTable(): List<Table> {
        val roleColumns = listOf(
            Column("name", VARCHAR, length = "255", comment = "名称"),
            Column("description", VARCHAR, length = "255", comment = "描述")
        )
        val roleTable = Table("role", "角色", roleColumns)
        val menuColumns = listOf(
            Column("parent_id", INT, length = "11", comment = "父id"),
            Column("name", VARCHAR, length = "255", comment = "名称"),
            Column("icon", VARCHAR, length = "255", comment = "图标"),
            Column("role", VARCHAR, length = "255", comment = "所属角色"),
            Column("href", VARCHAR, length = "255", comment = "地址"))
        val menuTable = Table("menu", "菜单", menuColumns)

        val permissionColumns = listOf(
            Column("role", VARCHAR, length = "255", comment = "角色"),
            Column("model", VARCHAR, length = "255", comment = "实体"),
            Column("operation", VARCHAR, length = "255", comment = "操作")
        )
        val permissionTable = Table("permission", "权限", permissionColumns)
        return listOf(roleTable, menuTable, permissionTable)
    }

    private fun parseServices(entities: List<Entity>, config: Config): List<Service> =
        entities.map {
            Service(config.servicePackagePath(), it, config.serviceImplPackagePath(), listOf(
                config.entityPackagePath(), config.commonPackagePath(), config.requestPackagePath(),
                    config.mapperPackagePath(), config.dtoPackagePath(), config.exceptionPackagePath()))
        }

    private fun parseMappers(entities: List<Entity>, config: Config): List<Mapper> =
        entities.map {
            Mapper(config.mapperPackagePath(), it.name,
                config.entityPackagePath(), config.dtoPackagePath(), config.requestPackagePath(), it)
        }

    private fun parseController(entities: List<Entity>, config: Config): List<Controller> =
        entities.map {
            Controller(config.controllerPackagePath(), it.name, listOf(
                config.entityPackagePath(), config.dtoPackagePath(), config.serviceImplPackagePath(),
                config.commonPackagePath(), config.requestPackagePath()), it)
        }
}
