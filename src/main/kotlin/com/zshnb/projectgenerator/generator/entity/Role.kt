package com.zshnb.projectgenerator.generator.entity

/**
 * 角色表
 * @param name 角色名
 * @param description 角色描述，前端显示用
 * @param menus 角色拥有的菜单
 * */
data class Role(val name: String, val description: String, val menus: List<Menu>)

data class Menu(
    val id: Int = 0,
    var parentId: Int = 0,
    val name: String = "",
    val icon: String = "",
    val href: String,
    var role: String,
    val bind: Boolean = true,
    val child: List<Menu> = emptyList()
)

/**
 * @param operation 增删改等操作
 * @param role 拥有权限的角色
 * @param model 前端概念中的页面
 * */
data class Permission(val operation: String, val role: String, val model: String) {
    override fun toString(): String {
        return "Permission(operation='$operation', role='$role', model='$model')"
    }
}
