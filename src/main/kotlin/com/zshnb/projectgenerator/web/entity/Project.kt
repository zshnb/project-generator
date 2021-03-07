package com.zshnb.projectgenerator.web.entity

import com.baomidou.mybatisplus.annotation.IdType.AUTO
import com.baomidou.mybatisplus.annotation.TableId

data class Project(
    @TableId(type = AUTO)
    val id: Int? = null,
    val name: String,
    val json: String,
    val description: String
)