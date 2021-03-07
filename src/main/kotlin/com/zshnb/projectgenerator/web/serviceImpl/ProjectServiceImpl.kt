package com.zshnb.projectgenerator.web.serviceImpl

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl
import com.zshnb.projectgenerator.web.common.Response
import com.zshnb.projectgenerator.web.entity.Project
import com.zshnb.projectgenerator.web.mapper.ProjectMapper
import com.zshnb.projectgenerator.web.request.AddOrUpdateProjectRequest
import org.springframework.stereotype.Service

@Service
class ProjectServiceImpl : ServiceImpl<ProjectMapper, Project>() {
    fun addOrUpdateProject(request: AddOrUpdateProjectRequest): Response<String> {
        val project = Project(name = request.name, json = request.json, description = request.description)
        save(project)
        return Response.ok()
    }
}