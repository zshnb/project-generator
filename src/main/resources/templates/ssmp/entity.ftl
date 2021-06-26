package ${packageName};

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
    ${table.comment}
*/
public class ${name?cap_first} extends Model<${name?cap_first}> {
    <#list fields! as field>
        /**
            ${field.column.comment}
        */
        <#if field.column.primary>
            @TableId(value = "${field.name}", type = IdType.AUTO)
        <#elseif field.type == 'LocalDateTime'>
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type == 'LocalDate'>
            @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
        </#if>
        <#if field.name == 'createAt'>
            @TableField(fill = FieldFill.INSERT)
        <#elseif field.name == 'updateAt'>
            @TableField(fill = FieldFill.INSERT_UPDATE)
        </#if>
        private ${field.type} ${field.name};
    </#list>
    <#list fields! as field>
        public ${field.type} get${field.name?cap_first}() {
            return this.${field.name};
        }
        public void set${field.name?cap_first}(${field.type} ${field.name}) {
            this.${field.name} = ${field.name};
        }
    </#list>
}