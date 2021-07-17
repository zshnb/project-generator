package com.zshnb.projectgenerator.generator.parser

import com.squareup.moshi.Moshi
import com.zshnb.projectgenerator.generator.entity.*
import com.zshnb.projectgenerator.generator.entity.ColumnType.*
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
            Table(it.name, it.comment, it.columns, it.permissions, it.columns.any { column -> column.searchable },
                it.enablePage, it.columns.any { column -> column.associate != null }, it.bindRoles)
        }
        project.roles.forEach { role ->
            role.menus.forEach {
                it.role = role.name
            }
        }
        return project
    }

    /**
     * 从[Table]解析出Java代码的实体类
     * */
    fun parseEntities(tables: List<Table>): List<Entity> {
        return tables.map {
            val fields = it.columns.map { column ->
                Field(column.name.toCamelCase(), column, typeUtil.convertColumnTypeToFieldType(column.type).description)
            }
            Entity(it.name.toCamelCase(), it, fields)
        }
    }

    /**
     * 创建角色，菜单，权限表
     * */
    private fun buildRoleAndMenuAndPermissionTable(): List<Table> {
        val roleColumns = listOf(
            Column("id", INT, "id主键", primary = true),
            Column("create_at", DATETIME, "创建时间"),
            Column("update_at", DATETIME, "更新时间"),
            Column("name", VARCHAR, "名称", "255"),
            Column("description", VARCHAR, "描述", "255")
        )
        val roleTable = Table("role", "角色", roleColumns)
        val menuColumns = listOf(
            Column("id", INT, "id主键", primary = true),
            Column("create_at", DATETIME, "创建时间"),
            Column("update_at", DATETIME, "更新时间"),
            Column("parent_id", INT, "父id"),
            Column("name", VARCHAR, "名称", "255"),
            Column("icon", VARCHAR, "图标", "255"),
            Column("role", VARCHAR, "所属角色", "255"),
            Column("href", VARCHAR, "地址", "255"))
        val menuTable = Table("menu", "菜单", menuColumns)

        val permissionColumns = listOf(
            Column("id", INT, "id主键", primary = true),
            Column("create_at", DATETIME, "创建时间"),
            Column("update_at", DATETIME, "更新时间"),
            Column("role", VARCHAR, "角色", "255"),
            Column("model", VARCHAR, "实体", "255"),
            Column("operation", VARCHAR, "操作", "255")
        )
        val permissionTable = Table("permission", "权限", permissionColumns)
        return listOf(roleTable, menuTable, permissionTable)
    }
}
