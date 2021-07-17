package com.zshnb.projectgenerator.generator.parser

import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component
import java.lang.RuntimeException

@Component
class FrontendParser(private val backendParser: BackendParser) {
    /**
    * 把前端传来的page对象完善，过滤掉不需要生成页面的entity
    * 为需要生成的page填充entity，过滤掉page中不需要生成表单项的field，然后为formItem填充field
    * */
    fun parsePages(project: Project): List<Page> {
        val pages = project.pages
        val entities = backendParser.parseEntities(project.tables)
        return entities.filter { it.table.enablePage }.mapIndexed { index, entity ->
            val page = pages[index]
            val formItems = page.form.items.mapIndexed { innerIndex, formItem ->
                getFormItem(formItem, entity.fields, innerIndex)
            }

            val enableTableFieldFields = entity.fields.filter { it.column.enableTableField || it.column.associate != null }
            val tableFields = page.table.fields.mapIndexed { innerIndex, tableField ->
                TableField(tableField.title, tableField.formItemClassName,
                    enableTableFieldFields[innerIndex], tableField.mappings)
            }
            val operations = entity.table.permissions.map { it.operations }
                .flatten().toSet().toList()
            Page(entity, FormComponent(formItems), TableComponent(tableFields, operations))
        }
    }

    private fun getFormItem(formItem: FormItem, fields: List<Field>, innerIndex: Int): FormItem =
        when (formItem) {
            is InputFormItem -> InputFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            is PasswordFormItem -> PasswordFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            is TextAreaFormItem -> TextAreaFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            is DateTimeFormItem -> DateTimeFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            is FileFormItem -> FileFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            is ImageFormItem -> ImageFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            is SelectFormItem -> SelectFormItem(fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require, formItem.label)
            is RadioFormItem -> RadioFormItem(fields[innerIndex], formItem.options, formItem.formItemClassName, formItem.require, formItem.label)
            is DateFormItem -> DateFormItem(fields[innerIndex], formItem.formItemClassName, formItem.require, formItem.label)
            else -> throw RuntimeException("un support form item: ${formItem::class.simpleName}")
        }

}
