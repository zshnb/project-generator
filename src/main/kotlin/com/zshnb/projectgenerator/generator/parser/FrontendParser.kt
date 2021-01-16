package com.zshnb.projectgenerator.generator.parser

import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class FrontendParser {
    fun parsePages(project: Project): List<Page> {
        val pages = project.pages
        return project.entities.filter { it.table.enablePage }.mapIndexed { index, entity ->
            val formItems = pages[index].form.formItems.mapIndexed { innerIndex, formItem ->
                when (formItem) {
                    is NullFormItem -> NullFormItem(entity.fields[innerIndex], formItem.formItemClassName)
                    is InputFormItem -> InputFormItem(entity.fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is TextAreaFormItem -> TextAreaFormItem(entity.fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is DateTimeFormItem -> DateTimeFormItem(entity.fields[innerIndex], formItem.formItemClassName, formItem.require)
                    is SelectFormItem -> SelectFormItem(entity.fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require)
                    is RadioFormItem -> RadioFormItem(entity.fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require)
                    else -> throw RuntimeException("un support form item: ${formItem::class.simpleName}")
                }
            }
            Page(entity, FormComponent(formItems))
        }
    }
}