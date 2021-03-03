package com.zshnb.projectgenerator.generator.parser

import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class FrontendParser {
    /**
    * 把前端传来的page对象完善，过滤掉不需要生成页面的entity
    * 为需要生成的page填充entity，过滤掉page中不需要生成表单项的field，然后为formItem填充field
    * */
    fun parsePages(project: Project): List<Page> {
        val pages = project.pages
        return project.entities.mapIndexedNotNull { index, entity ->
            if (!entity.table.enablePage) {
                null
            } else {
                val fields = entity.fields.filter { it.column.enableFormItem }
                val formItems = pages[index].form.formItems.mapIndexed { innerIndex, formItem ->
                    when (formItem) {
                        is InputFormItem -> InputFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                        is PasswordFormItem -> PasswordFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                        is TextAreaFormItem -> TextAreaFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                        is DateTimeFormItem -> DateTimeFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                        is FileFormItem -> FileFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                        is SelectFormItem -> SelectFormItem(fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require)
                        is RadioFormItem -> RadioFormItem(fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require)
                        is DateFormItem -> DateFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require)
                        else -> throw RuntimeException("un support form item: ${formItem::class.simpleName}")
                    }
                }
                Page(entity, FormComponent(formItems))
            }
        }
    }
}
