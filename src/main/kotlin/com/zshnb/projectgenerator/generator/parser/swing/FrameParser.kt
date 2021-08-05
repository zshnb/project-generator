package com.zshnb.projectgenerator.generator.parser.swing

import com.zshnb.projectgenerator.generator.entity.swing.*
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.parser.web.BackendParser
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class FrameParser(private val backendParser: BackendParser) {
    fun parseFrames(swingProject: SwingProject): List<Frame> {
        val frames = swingProject.frames
        val entities = backendParser.parseEntities(swingProject.tables)
        return entities.filter { it.table.enablePage }.mapIndexed { index, entity ->
            val frame = frames[index]
            val items = frame.items.mapIndexed { innerIndex, item ->
                getFrameItem(item, entity.fields, innerIndex)
            }
            Frame(entity, items)
        }

    }

    private fun getFrameItem(frameItem: FrameItem, fields: List<Field>, innerIndex: Int): FrameItem =
        when (frameItem) {
            is TextFieldFrameItem -> TextFieldFrameItem(fields[innerIndex], frameItem.className)
            is PasswordFrameItem -> PasswordFrameItem(fields[innerIndex], frameItem.className)
            is RadioFrameItem -> RadioFrameItem(fields[innerIndex], frameItem.className, frameItem.options)
            is SelectFrameItem -> SelectFrameItem(fields[innerIndex], frameItem.className, frameItem.options)
            else -> throw RuntimeException("un support form item: ${frameItem::class.simpleName}")
        }

}