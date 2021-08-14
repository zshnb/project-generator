package com.zshnb.projectgenerator.generator.parser.web

import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.entity.web.ColumnType.*
import com.zshnb.projectgenerator.generator.extension.toCamelCase
import com.zshnb.projectgenerator.generator.util.TypeUtil
import org.springframework.stereotype.Component

/**
 * 从[WebProject]的json中解析出项目的各个部分
 * */
@Component
class BackendParser(private val typeUtil: TypeUtil) {
    fun parseProject(webProject: WebProject): WebProject {
        webProject.tables = parseTables(webProject.tables)
        webProject.roles.forEach { role ->
            procedureMenus(role)
        }
        return webProject
    }

    fun procedureMenus(role: Role) {
        role.menus.forEach {
            it.role = role.name
        }
    }

    fun parseTables(tables: List<Table>): List<Table> {
        val resultTables = tables + buildRoleAndMenuAndPermissionTable()
        return resultTables.map {
            val columns = if (it.bindUser != null) {
                it.columns.toMutableList().apply {
                    add(Column("user_id", INT, "user外键", enableFormItem = false,
                        enableTableField = false))
                }
            } else {
                it.columns
            }
            Table(it.name, it.comment, columns, it.permissions, it.columns.any { column -> column.searchable },
                it.enablePage, it.columns.any { column -> column.associate != null }, it.bindRoles, it.bindUser)
        }
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
            Column("id", INT, "id主键", primary = true, enableFormItem = false, enableTableField = false),
            Column("create_at", DATETIME, "创建时间", enableFormItem = false, enableTableField = false),
            Column("update_at", DATETIME, "更新时间", enableFormItem = false, enableTableField = false),
            Column("name", VARCHAR, "名称", "255", enableFormItem = false, enableTableField = false),
            Column("description", VARCHAR, "描述", "255", enableFormItem = false, enableTableField = false))
        val roleTable = Table("role", "角色", roleColumns, enablePage = false)
        val menuColumns = listOf(
            Column("id", INT, "id主键", primary = true, enableFormItem = false, enableTableField = false),
            Column("create_at", DATETIME, "创建时间", enableFormItem = false, enableTableField = false),
            Column("update_at", DATETIME, "更新时间", enableFormItem = false, enableTableField = false),
            Column("parent_id", INT, "父id", enableFormItem = false, enableTableField = false),
            Column("name", VARCHAR, "名称", "255", enableFormItem = false, enableTableField = false),
            Column("icon", VARCHAR, "图标", "255", enableFormItem = false, enableTableField = false),
            Column("role", VARCHAR, "所属角色", "255", enableFormItem = false, enableTableField = false),
            Column("href", VARCHAR, "地址", "255", enableFormItem = false, enableTableField = false))
        val menuTable = Table("menu", "菜单", menuColumns, enablePage = false)

        val permissionColumns = listOf(
            Column("id", INT, "id主键", primary = true, enableFormItem = false, enableTableField = false),
            Column("create_at", DATETIME, "创建时间", enableFormItem = false, enableTableField = false),
            Column("update_at", DATETIME, "更新时间", enableFormItem = false, enableTableField = false),
            Column("role", VARCHAR, "角色", "255", enableFormItem = false, enableTableField = false),
            Column("model", VARCHAR, "实体", "255", enableFormItem = false, enableTableField = false),
            Column("operation", VARCHAR, "操作", "255", enableFormItem = false, enableTableField = false))
        val permissionTable = Table("permission", "权限", permissionColumns, enablePage = false)
        return listOf(roleTable, menuTable, permissionTable)
    }
}
