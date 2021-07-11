package ${packageName};

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
<#function literalize(str)>
    <#if config.database == "MYSQL">
        <#return '`${str}`'>
    <#elseif config.database == "SQLSERVER">
        <#return '[${str}]'>
    </#if>
</#function>
/**
    ${entity.table.comment}
*/
@TableName("${literalize(entity.table.name)}")
public class ${entity.name?cap_first} extends Model<${entity.name?cap_first}> {
    <#list entity.fields as field>
        /**
            ${field.column.comment}
        */
        <#if field.column.primary>
            @TableId(value = "${field.name}", type = IdType.AUTO)
            @OrderBy(isDesc = false)
        <#elseif field.type == 'LocalDateTime'>
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type == 'LocalDate'>
            @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
        </#if>
        <#if field.name == 'createAt'>
            @TableField(fill = FieldFill.INSERT)
        <#elseif field.name == 'updateAt'>
            @TableField(fill = FieldFill.INSERT_UPDATE)
        <#elseif !field.column.primary>
            @TableField("${literalize(field.column.name)}")
        </#if>
        private ${field.type} ${field.name};
    </#list>
    <#list entity.fields as field>
        public ${field.type} get${field.name?cap_first}() {
            return this.${field.name};
        }
        public void set${field.name?cap_first}(${field.type} ${field.name}) {
            this.${field.name} = ${field.name};
        }
    </#list>
}