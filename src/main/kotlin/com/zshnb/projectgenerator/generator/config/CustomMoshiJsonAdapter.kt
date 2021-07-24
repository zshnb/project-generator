package com.zshnb.projectgenerator.generator.config

import com.squareup.moshi.*
import com.zshnb.projectgenerator.generator.entity.web.*
import com.zshnb.projectgenerator.generator.util.TypeUtil
import org.springframework.stereotype.Component

/**
 * @see [https://bladecoder.medium.com/advanced-json-parsing-techniques-using-moshi-and-kotlin-daf56a7b963d]
 * */
@Component
class FormItemAdapter : JsonAdapter<FormItem>() {
    override fun fromJson(reader: JsonReader): FormItem? {
        reader.beginObject()
        var label = ""
        var formItemClassName = ""
        var require = false
        val options = mutableListOf<Option>()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "formItemClassName" -> formItemClassName = reader.nextString()
                "require" -> require = reader.nextBoolean()
                "label" -> label = reader.nextString()
                "options" -> {
                    reader.beginArray()
                    while (reader.hasNext()) {
                        var title = ""
                        var value = ""
                        reader.beginObject()
                        while (reader.hasNext()) {
                            when (reader.nextName()) {
                                "title" -> title = reader.nextString()
                                "value" -> value = reader.nextString()
                                else -> reader.skipValue()
                            }
                        }
                        reader.endObject()
                        options.add(Option(title, value))
                    }
                    reader.endArray()
                }
            }
        }
        reader.endObject()
        return when (formItemClassName) {
            InputFormItem::class.qualifiedName -> InputFormItem(null, formItemClassName, require, label)
            PasswordFormItem::class.qualifiedName -> PasswordFormItem(null, formItemClassName, require, label)
            DateTimeFormItem::class.qualifiedName -> DateTimeFormItem(null, formItemClassName, require, label)
            DateFormItem::class.qualifiedName -> DateFormItem(null, formItemClassName, require, label)
            TextAreaFormItem::class.qualifiedName -> TextAreaFormItem(null, formItemClassName, require, label)
            RadioFormItem::class.qualifiedName -> RadioFormItem(null, options, formItemClassName, require, label)
            FileFormItem::class.qualifiedName -> FileFormItem(null, formItemClassName, require, label)
            ImageFormItem::class.qualifiedName -> ImageFormItem(null, formItemClassName, require, label)
            SelectFormItem::class.qualifiedName -> SelectFormItem(null, options, formItemClassName, require, label)
            else -> null
        }
    }

    override fun toJson(writer: JsonWriter, value: FormItem?) {
        TODO("Not yet implemented")
    }
}

@Component
class AssociateResultColumnAdapter(private val typeUtil: TypeUtil) : JsonAdapter<AssociateResultColumn>() {
    override fun fromJson(reader: JsonReader): AssociateResultColumn? {
        val associateResultColumn = AssociateResultColumn()
        reader.beginObject()
        while (reader.hasNext()) {
            with(associateResultColumn) {
                when (reader.nextName()) {
                    "originColumnName" -> originColumnName = reader.nextString()
                    "tableFieldTitle" -> tableFieldTitle = reader.nextString()
                    "columnType" -> {
                        columnType = ColumnType.valueOf(reader.nextString().toUpperCase())
                        fieldType = typeUtil.convertColumnTypeToFieldType(columnType)
                    }
                }
            }
        }
        reader.endObject()
        return associateResultColumn
    }

    override fun toJson(p0: JsonWriter, p1: AssociateResultColumn?) {
        TODO("Not yet implemented")
    }
}