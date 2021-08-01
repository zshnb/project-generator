package com.zshnb.projectgenerator.generator.entity.swing

import com.zshnb.projectgenerator.generator.entity.web.Table

data class SwingProject(val tables: List<Table>,
                        val frames: List<Frame>) {
}
