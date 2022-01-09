package ${packageName};

import java.time.LocalDateTime;
/**
    ${entity.table.comment}
*/
public class ${entity.name?cap_first} {
    <#if entity.table.name == "user" && !entity.table.enablePage>
        public User() {
        }
        public User(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
        }
    </#if>
    <#if entity.table.enablePage>
    public ${entity.name?cap_first}() {}
    <#if (entity.fields?filter(it -> it.column.enableFormItem)?size > 0)>
        <#assign params>
            <#list entity.fields?filter(it -> it.column.enableFormItem) as field>
                ${field.type} ${field.name}<#if field_has_next>, </#if><#t>
            </#list>
        </#assign>

        public ${entity.name?cap_first}(${params}) {
        <#list entity.fields?filter(it -> it.column.enableFormItem) as field>
            this.${field.name} = ${field.name};
        </#list>
        }
    </#if>

    </#if>
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