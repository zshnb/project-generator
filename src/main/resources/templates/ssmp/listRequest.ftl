package ${packageName};
import ${commonPackageName}.PageRequest;

public class List${entity.table.name?cap_first}Request extends PageRequest {
    <#if entity.table.name == "menu">
    private String role = "";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    </#if>
    <#if entity.table.searchable>
        <#list entity.fields as field>
            <#if field.column.searchable>
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
                </#if>
            </#if>
        </#list>
    </#if>
}
