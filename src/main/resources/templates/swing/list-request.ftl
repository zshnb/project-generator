package ${packageName};
import java.time.LocalDateTime;
import java.time.LocalDate;

public class List${entity.name?cap_first}Request {
    <#if entity.name == "menu">
    private String role = "";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    </#if>
    <#list entity.fields?filter(f -> f.column.searchable) as field>
        <#assign fieldName>${field.name}</#assign>
        <#if field.type == "String">
            private String ${fieldName} = "";
            public String get${fieldName?cap_first}() {
                return ${fieldName};
            }
            public void set${fieldName?cap_first}(String ${fieldName}) {
               this.${fieldName} = ${fieldName};
            }
        <#elseif field.type == "Integer">
            private Integer ${fieldName} = 0;
            public Integer get${fieldName?cap_first}() {
               return ${fieldName};
            }
            public void set${fieldName?cap_first}(Integer ${fieldName}) {
                this.${fieldName} = ${fieldName};
            }
        <#elseif field.type == "LocalDateTime">
            private LocalDateTime ${fieldName};
            public LocalDateTime get${fieldName?cap_first}() {
                return ${fieldName};
            }
            public void set${fieldName?cap_first}(LocalDateTime ${fieldName}) {
                this.${fieldName} = ${fieldName};
            }
        <#elseif field.type == "LocalDate">
            private LocalDate ${fieldName};
            public LocalDate get${fieldName?cap_first}() {
                return ${fieldName};
            }
            public void set${fieldName?cap_first}(LocalDate ${fieldName}) {
                this.${fieldName} = ${fieldName};
            }
        </#if>
    </#list>
}
