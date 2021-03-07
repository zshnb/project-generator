package com.zshnb.projectgenerator.web.mapper

import com.baomidou.mybatisplus.core.mapper.BaseMapper
import com.zshnb.projectgenerator.web.entity.Project
import org.apache.ibatis.annotations.Mapper

@Mapper
interface ProjectMapper : BaseMapper<Project>{
}