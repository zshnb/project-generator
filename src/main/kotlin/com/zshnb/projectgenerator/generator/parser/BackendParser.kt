package com.zshnb.projectgenerator.generator.parser

import com.google.gson.Gson
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
class BackendParser(private val gson: Gson,
                    private val typeUtil: TypeUtil) {
    fun parseProject(json: String): Project {
        val project = gson.fromJson(json, Project::class.java)
        project.tables = project.tables + buildRoleAndMenuAndPermissionTable()
        val userTable = project.tables.find { it.name == "user" }
        if (userTable != null) {
            userTable.columns = (userTable.columns.toSet() + setOf(
                Column("username", VARCHAR, length = 255),
                Column("password", VARCHAR, length = 255),
                Column("role", VARCHAR, length = 255)
            )).toList()
        } else {
            project.tables = project.tables + Table("user", listOf(
                Column("username", VARCHAR, length = 255),
                Column("password", VARCHAR, length = 255),
                Column("role", VARCHAR, length = 255)
            ))
        }
        project.tables.forEach { table ->
            table.columns = (table.columns.toSet() + setOf(
                Column("id", INT, length = 11, primary = true),
                Column("create_at", DATETIME, length = 0),
                Column("update_at", DATETIME, length = 0)
            )).toList()
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
                Field(column.name.toCamelCase(), typeUtil.convertColumnTypeToFieldType(column.type).description,
                    column.comment, column.primary)
            }
            Entity(config.entityPackagePath(), it.name.toCamelCase(), fields)
        }
    }

    /**
     * 创建角色，菜单，权限表
     * */
    private fun buildRoleAndMenuAndPermissionTable(): List<Table> {
        val roleColumns = listOf(
            Column("id", INT, length = 11, primary = true),
            Column("name", VARCHAR, length = 255),
            Column("description", VARCHAR, length = 255)
        )
        val roleTable = Table("role", roleColumns)
        val menuColumns = listOf(
            Column("id", INT, length = 11, primary = true),
            Column("parent_id", INT, length = 11),
            Column("name", VARCHAR, length = 255),
            Column("icon", VARCHAR, length = 255),
            Column("role", VARCHAR, length = 255),
            Column("href", VARCHAR, length = 255))
        val menuTable = Table("menu", menuColumns)

        val permissionColumns = listOf(
            Column("id", INT, length = 11, primary = true),
            Column("role", VARCHAR, length = 255),
            Column("model", VARCHAR, length = 255),
            Column("operation", VARCHAR, length = 255)
        )
        val permissionTable = Table("permission", permissionColumns)
        return listOf(roleTable, menuTable, permissionTable)
    }

    /**
     *
     * */
    private fun parseServices(entities: List<Entity>, config: Config): List<Service> =
        entities.map {
            Service(config.servicePackagePath(), it.name, config.serviceImplPackagePath(), listOf(
                config.entityPackagePath(), config.commonPackagePath(), config.requestPackagePath(),
                    config.mapperPackagePath(), config.dtoPackagePath()))
        }

    private fun parseMappers(entities: List<Entity>, config: Config): List<Mapper> =
        entities.map {
            Mapper(config.mapperPackagePath(), it.name, config.entityPackagePath())
        }

    private fun parseController(entities: List<Entity>, config: Config): List<Controller> =
        entities.map {
            Controller(config.controllerPackagePath(), it.name, listOf(
                config.entityPackagePath(), config.dtoPackagePath(), config.serviceImplPackagePath(),
                config.commonPackagePath(), config.requestPackagePath()))
        }
}