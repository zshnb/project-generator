package com.zshnb.projectgenerator.web.request

data class AddOrUpdateProjectRequest(
    val name: String,
    val json: String,
    val description: String = ""
) : BaseRequest()