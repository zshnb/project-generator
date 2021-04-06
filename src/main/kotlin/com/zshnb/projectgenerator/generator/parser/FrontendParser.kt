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
        return project.entities.filter { it.table.enablePage }.mapIndexed { index, entity ->
            val page = pages[index]
            val enableFormItemFields = entity.fields.filter { it.column.enableFormItem }
            val formItems = page.form.items.mapIndexed { innerIndex, formItem ->
                when (formItem) {
                    is InputFormItem -> InputFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    is PasswordFormItem -> PasswordFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    is TextAreaFormItem -> TextAreaFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    is DateTimeFormItem -> DateTimeFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    is FileFormItem -> FileFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    is ImageFormItem -> ImageFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    is SelectFormItem -> SelectFormItem(enableFormItemFields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require, formItem.label)
                    is RadioFormItem -> RadioFormItem(enableFormItemFields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require, formItem.label)
                    is DateFormItem -> DateFormItem(enableFormItemFields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
                    else -> throw RuntimeException("un support form item: ${formItem::class.simpleName}")
                }
            }
            val enableTableFieldFields = entity.fields.filter { it.column.enableTableField || it.column.associate != null }
            val tableFields = page.table.fields.mapIndexed { innerIndex, tableField ->
                TableField(tableField.title, tableField.formItemClassName,
                    enableTableFieldFields[innerIndex], tableField.mappings)
            }
            Page(entity, FormComponent(formItems), TableComponent(tableFields, entity.table.permissions.map { it.operation }))
        }
    }
}
