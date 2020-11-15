package com.zshnb.projectgenerator.generator.parser

import com.google.gson.Gson
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.extension.*
import com.zshnb.projectgenerator.generator.util.TypeUtil
import com.zshnb.projectgenerator.generator.util.toCamelCase
import org.springframework.stereotype.Component

@Component
class BackendParser(private val gson: Gson,
                    private val typeUtil: TypeUtil) {
    fun parseProject(json: String): Project {
        val project = gson.fromJson<Project>(json, Project::class.java)
        project.tables = project.tables + buildRoleAndMenuAndPermissionTable()
        val userTable = project.tables.find { it.name == "user" }
        if (userTable != null) {
            userTable.columns = (userTable.columns.toSet() + setOf(
                Column("username", ColumnType.VARCHAR, length = 255),
                Column("password", ColumnType.VARCHAR, length = 255),
                Column("role", ColumnType.VARCHAR, length = 255)
            )).toList()
        } else {
            project.tables = project.tables + Table("user", listOf(
                Column("id", ColumnType.INT, length = 11, primary = true),
                Column("username", ColumnType.VARCHAR, length = 255),
                Column("password", ColumnType.VARCHAR, length = 255),
                Column("role", ColumnType.VARCHAR, length = 255)
            ))
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

    private fun parseEntities(tables: List<Table>, config: Config): List<Entity> {
        return tables.map {
            val fields = it.columns.map { column ->
                Field(
                    column.name.toCamelCase(), typeUtil.convertColumnTypeToFieldType(column.type).description,
                    column.comment, column.primary
                )
            }
            Entity(config.entityPackagePath(), it.name, fields)
        }
    }

    private fun buildRoleAndMenuAndPermissionTable(): List<Table> {
        val roleColumns = listOf(
            Column("id", ColumnType.INT, length = 11, primary = true),
            Column("name", ColumnType.VARCHAR, length = 255)
        )
        val roleTable = Table("role", roleColumns)
        val menuColumns = listOf(
            Column("id", ColumnType.INT, length = 11, primary = true),
            Column("parent_id", ColumnType.INT, length = 11),
            Column("name", ColumnType.VARCHAR, length = 255),
            Column("icon", ColumnType.VARCHAR, length = 255),
            Column("role", ColumnType.VARCHAR, length = 255),
            Column("href", ColumnType.VARCHAR, length = 255))
        val menuTable = Table("menu", menuColumns)

        val permissionColumns = listOf(
            Column("id", ColumnType.INT, length = 11, primary = true),
            Column("role", ColumnType.VARCHAR, length = 255),
            Column("model", ColumnType.VARCHAR, length = 255),
            Column("operation", ColumnType.VARCHAR, length = 255)
        )
        val permissionTable = Table("permission", permissionColumns)
        return listOf(roleTable, menuTable, permissionTable)
    }

    private fun parseServices(entities: List<Entity>, config: Config): List<Service> =
        entities.map {
            Service(config.servicePackagePath(), it.name, config.serviceImplPackagePath(), listOf(config.entityPackagePath(),
                config.commonPackagePath(), config.requestPackagePath(), config.mapperPackagePath(), config.dtoPackagePath()))
        }

    private fun parseMappers(entities: List<Entity>, config: Config): List<Mapper> =
        entities.map {
            Mapper(config.mapperPackagePath(), it.name, config.entityPackagePath())
        }

    private fun parseController(entities: List<Entity>, config: Config): List<Controller> =
        entities.map {
            Controller(config.controllerPackagePath(), it.name, listOf(config.entityPackagePath(), config.dtoPackagePath(),
                config.serviceImplPackagePath(), config.commonPackagePath(), config.requestPackagePath()))
        }
}