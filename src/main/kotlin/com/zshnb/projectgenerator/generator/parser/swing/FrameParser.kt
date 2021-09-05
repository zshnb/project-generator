package com.zshnb.projectgenerator.generator.parser.swing

import com.zshnb.projectgenerator.generator.entity.swing.*
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.parser.web.BackendParser
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class FrameParser {
    fun parseFrames(frames: List<Frame>, entities: List<Entity>): List<Frame> {
        return entities.filter { it.table.enablePage }.mapIndexed { index, entity ->
            val frame = frames[index]
            val items = frame.items.mapIndexed { innerIndex, item ->
                getFrameItem(item, entity.fields, innerIndex)
            }
            val operations = entity.table.permissions.map { it.operations }
                .flatten()
                .distinctBy { it.value }
            Frame(entity, items, operations)
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