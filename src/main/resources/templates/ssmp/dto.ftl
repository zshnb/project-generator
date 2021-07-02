package ${packageName};

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;

public class ${entity.name?cap_first}Dto {
    <#list entity.fields! as field>
        <#if field.type == 'LocalDateTime'>
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type == 'LocalDate'>
            @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
        </#if>
        <#if field.column.associate??>
            <#list field.column.associate.associateResultColumns as column>
                private String ${column.aliasColumnName};
                public String get${column.aliasColumnName?cap_first}() {
                    return this.${column.aliasColumnName};
                }
                public void set${column.aliasColumnName?cap_first}(String ${column.aliasColumnName}) {
                    this.${column.aliasColumnName} = ${column.aliasColumnName};
                }
            </#list>
        </#if>
        private ${field.type} ${field.name};
        public ${field.type} get${field.name?cap_first}() {
            return this.${field.name};
        }
        public void set${field.name?cap_first}(${field.type} ${field.name}) {
            this.${field.name} = ${field.name};
        }
    </#list>
}