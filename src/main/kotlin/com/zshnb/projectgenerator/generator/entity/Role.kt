package com.zshnb.projectgenerator.generator.entity

/**
 * 权限表
 * @param name 权限名
 * @param description 权限描述，前端显示用
 * @param menus 权限拥有的菜单
 * */
data class Role(val name: String, val description: String, val menus: List<Menu>)

data class Menu(
    val id: Int = 0,
    val parentId: Int = 0,
    val name: String = "",
    val icon: String = "",
    val href: String = "",
    var role: String = "",
    val child: List<Menu> = emptyList()
)

/**
 * [operation]: 增删改等操作
 * [role]: 角色
 * [model]: 前端概念中的页面
 * */
data class Permission(val operation: String, val role: String, val model: String) {
    override fun toString(): String {
        return "Permission(operation='$operation', role='$role', model='$model')"
    }
}