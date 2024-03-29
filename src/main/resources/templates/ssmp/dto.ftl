package ${packageName};

import java.time.LocalDateTime;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
<#function camelize(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncap_first>
</#function>
public class ${entity.name?cap_first}Dto {
    <#list entity.fields! as field>
        <#if field.type == 'LocalDateTime'>
            @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
        <#elseif field.type == 'LocalDate'>
            @JsonFormat(pattern="yyyy-MM-dd",timezone = "GMT+8")
        </#if>
        <#if field.column.associate??>
            <#list field.column.associate.associateResultColumns as column>
                <#assign type>${column.fieldType.description}</#assign>
                <#assign aliasColumnName = camelize(field.column.associate.targetTableName + "_" + column.originColumnName)/>
                private ${type} ${aliasColumnName};
                public ${type} get${aliasColumnName?cap_first}() {
                    return this.${aliasColumnName};
                }
                public void set${aliasColumnName?cap_first}(${type} ${aliasColumnName}) {
                    this.${aliasColumnName} = ${aliasColumnName};
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