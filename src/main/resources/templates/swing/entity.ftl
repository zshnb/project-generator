package ${packageName};

import java.time.LocalDateTime;
/**
    ${entity.table.comment}
*/
public class ${entity.name?cap_first} {
    <#list entity.fields as field>
        /**
            ${field.column.comment}
        */
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