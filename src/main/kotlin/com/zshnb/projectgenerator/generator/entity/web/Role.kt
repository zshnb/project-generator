package com.zshnb.projectgenerator.generator.entity.web

import com.squareup.moshi.Json

/**
 * 角色表
 * @param name 角色名
 * @param description 角色描述，前端显示用
 * @param menus 角色拥有的菜单
 * */
data class Role(val name: String, val description: String, val menus: List<Menu>)

/**
 * 菜单表
 * @param name 名称
 * @param href 路径
 * @param bind 是否与实体绑定，绑定会根据实体名称生成菜单，否则使用用户填写的路径，并对应生成空页面
 * @param child 子菜单
 * */
data class Menu(
    val id: Int = 0,
    var parentId: Int = 0,
    val name: String,
    val icon: String = "",
    val href: String,
    var role: String,
    val bind: Boolean = true,
    val child: List<Menu> = emptyList()
)

/**
 * 操作
 * @param description 描述，用来显示按钮
 * @param value 值，存储进数据库以及前端判断用
 * @param position 位于页面上的操作栏还是单元格
 * @param custom 是否为自定义操作
 * @param type 操作行为
 * @param detail 自定义操作详细信息
 * */
data class Operation(val description: String,
                     val value: String,
                     val position: OperationPosition,
                     val custom: Boolean = false,
                     val type: OperationType?,
                     val detail: OperationDetail?)

/**
 * 自定义ajax操作详细信息
 * @param httpMethod 请求的类型
 * @param pathVariable 是否携带路径参数
 * */
data class OperationDetail(val httpMethod: HttpMethod,
                           val pathVariable: Boolean)

enum class HttpMethod {
    GET,
    POST,
    PUT,
    DELETE
}

enum class OperationType {
    NEW_PAGE,
    AJAX
}

enum class OperationPosition(val code: Int, val description: String) {
    @Json(name = "toolbar")
    TOOLBAR(0, "toolbar"),

    @Json(name = "toolColumn")
    TOOL_COLUMN(1, "toolColumn")
    ;

    override fun toString(): String {
        return description
    }
}

/**
 * @param operations 操作
 * @param role 拥有权限的角色
 * @param model 前端概念中的页面
 * */
data class Permission(val operations: List<Operation>, val role: String, val model: String?)
