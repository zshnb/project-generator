package ${packageName};

import java.time.LocalDateTime;
import java.time.LocalDate;

public class ${entity.name?cap_first}Dto {
    <#list entity.fields! as field>
        <#if field.column.associate??>
            <#list field.column.associate.associateResultColumns as column>
                <#assign type>${column.fieldType.description}</#assign>
                <#assign aliasColumnName = "${field.column.associate.targetTableName + column.originColumnName?cap_first}">
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