package com.zshnb.projectgenerator.generator.constant

import kotlin.random.Random

class FrontendFreeMarkerFileConstant {
    companion object {
        const val LAY_UI_INDEX_PAGE = "layui/index.ftl"
        const val LAY_UI_ADD_PAGE = "layui/page/add.ftl"
        const val LAY_UI_EDIT_PAGE = "layui/page/edit.ftl"
        const val LAY_UI_DETAIL_PAGE = "layui/page/detail.ftl"
        const val LAY_UI_TABLE_PAGE = "layui/page/table.ftl"
        const val LAY_UI_EMPTY_PAGE = "layui/page/empty-page.ftl"

        val LAYUI_LOGIN_PAGES = listOf("layui/login1.ftl", "layui/login2.ftl", "layui/login3.ftl")
        val LAYUI_REGISTER_PAGES = listOf("layui/register1.ftl", "layui/register2.ftl", "layui/register3.ftl")
    }
}
