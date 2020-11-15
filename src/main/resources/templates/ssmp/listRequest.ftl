package ${packageName};
import ${commonPackageName}.PageRequest;

public class List${name?cap_first}Request extends PageRequest {
    <#if name == "menu">
    private String role = "";

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
    </#if>
}
