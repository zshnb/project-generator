package com.zshnb.projectgenerator.generator.config

import com.squareup.moshi.*
import com.zshnb.projectgenerator.generator.entity.*
import org.springframework.stereotype.Component

/**
 * @see https://bladecoder.medium.com/advanced-json-parsing-techniques-using-moshi-and-kotlin-daf56a7b963d
 * */
@Component
class FormItemAdapter : JsonAdapter<FormItem>() {
    override fun fromJson(reader: JsonReader): FormItem? {
        reader.beginObject()
        var formItemClassName = ""
        var require = false
        val options = mutableListOf<Option>()
        while (reader.hasNext()) {
            when (reader.nextName()) {
                "formItemClassName" -> formItemClassName = reader.nextString()
                "require" -> require = reader.nextBoolean()
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
            InputFormItem::class.qualifiedName -> InputFormItem(null, formItemClassName, require)
            PasswordFormItem::class.qualifiedName -> PasswordFormItem(null, formItemClassName, require)
            DateTimeFormItem::class.qualifiedName -> DateTimeFormItem(null, formItemClassName, require)
            DateFormItem::class.qualifiedName -> DateFormItem(null, formItemClassName, require)
            TextAreaFormItem::class.qualifiedName -> TextAreaFormItem(null, formItemClassName, require)
            RadioFormItem::class.qualifiedName -> RadioFormItem(null, options, formItemClassName, require)
            FileFormItem::class.qualifiedName -> FileFormItem(null, formItemClassName, require)
            SelectFormItem::class.qualifiedName -> SelectFormItem(null, options, formItemClassName, require)
            else -> throw Exception("un support form item: $formItemClassName")
        }
    }

    override fun toJson(writer: JsonWriter, value: FormItem?) {
        TODO("Not yet implemented")
    }
}
