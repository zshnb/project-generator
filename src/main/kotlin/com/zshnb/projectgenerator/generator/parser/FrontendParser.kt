package com.zshnb.projectgenerator.generator.parser

import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class FrontendParser {
    fun parsePages(project: Project): List<Page> {
        val pages = project.pages
        return project.entities.filter { it.table.enablePage }.mapIndexed { index, entity ->
            val fields = entity.fields.filter { it.column.enableFormItem }
            val formItems = pages[index].form.formItems.mapIndexed { innerIndex, formItem ->
                when (formItem) {
                    is InputFormItem -> InputFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is TextAreaFormItem -> TextAreaFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is DateTimeFormItem -> DateTimeFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is FileFormItem -> FileFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is SelectFormItem -> SelectFormItem(fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require)
                    is RadioFormItem -> RadioFormItem(fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require)
                    else -> throw RuntimeException("un support form item: ${formItem::class.simpleName}")
                }
            }
            Page(entity, FormComponent(formItems))
        }
    }
}