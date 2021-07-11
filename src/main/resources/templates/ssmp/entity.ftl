package ${entity.packageName};

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;
<#assign tableName>
    <#if config.database == "MYSQL">
        `${entity.table.name}`
    <#elseif config.database == "SQLSERVER">
        [${entity.table.name}]
    </#if>
</#assign>
/**
    ${entity.table.comment}
*/
@TableName("<#compress>${tableName}</#compress>")
public class ${entity.name?cap_first} extends Model<${entity.name?cap_first}> {
    <#list entity.fields as field>
        <#assign fieldName>
            <#if config.database == "MYSQL">
                `${field.column.name}`
            <#elseif config.database == "SQLSERVER">
                [${field.column.name}]
            </#if>
        </#assign>
        /**
            ${field.column.comment}
        */
        <#if field.column.primary>
            @TableId(value = "${field.name}", type = IdType.AUTO)
            @OrderBy
        <#elseif field.type == 'LocalDateTime'>
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type == 'LocalDate'>
            @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
        </#if>
        <#if field.name == 'createAt'>
            @TableField(fill = FieldFill.INSERT)
        <#elseif field.name == 'updateAt'>
            @TableField(fill = FieldFill.INSERT_UPDATE)
        <#elseif !field.column.primary  >
            @TableField("<#compress>${fieldName}</#compress>")
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