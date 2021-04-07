package ${packageName};

<#list dependencies! as d>
import ${d}.*;
</#list>
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/${name}")
public class ${name?capFirst}Controller {
    <#assign service>${name}Service</#assign>
    <#assign className>${name?capFirst}</#assign>
    @Autowired
    private ${className}ServiceImpl ${service};

    <#if name != "permission">
    @Autowired
    private PermissionServiceImpl permissionService;
    </#if>

    <#if name == "menu">
    @PostMapping("/list")
    @ResponseBody
    public ListResponse<MenuDto> list(HttpSession session) {
        String role = ((User) session.getAttribute("user")).getRole();
        return menuService.list(role);
    }
    <#else>
    @PostMapping("/add")
    @ResponseBody
    public Response<${className}> add(@RequestBody ${className} request) {
        ${className} ${name} = ${service}.add(request);
        return Response.ok(${name});
    }

    @PutMapping("/update")
    @ResponseBody
    public Response<${className}> update(@RequestBody ${className} old) {
        ${className} ${name} = ${service}.update(old);
        return Response.ok(${name});
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        ${service}.delete(id);
        return Response.ok();
    }

    <#assign returnClass>
        <#if entity.table.associate>
            ${name?capFirst}Dto
        <#else>
            ${className}
        </#if>
    </#assign>
    @PostMapping("/page")
    @ResponseBody
    public ListResponse<<#compress>${returnClass}</#compress>> page(@RequestBody List${className}Request request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return ${service}.list(request);
    }

    @GetMapping("/list")
    @ResponseBody
    public ListResponse<<#compress>${returnClass}</#compress>> list() {
        return ${service}.listAll();
    }

    @GetMapping("/tablePage")
    public String tablePage(HttpSession httpSession, Model model) {
        String role = ((User) httpSession.getAttribute("user")).getRole();
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", role)
            .eq("model", "${name}"));
        model.addAttribute("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return "page/${name}/table";
    }

    @GetMapping("/addPage")
    public String addPage () {
        return "page/${name}/add";
    }

    @GetMapping("/editPage/{id}")
    public String editPage(@PathVariable int id, Model model) {
        ${className} ${name} = ${service}.getById(id);
        model.addAttribute("${name}", ${name});
        return "page/${name}/edit";
    }
    @GetMapping("/detailPage/{id}")
    public String detailPage(@PathVariable int id, Model model) {
        ${className} ${name} = ${service}.getById(id);
        model.addAttribute("${name}", ${name});
        return "page/${name}/detail";
    }
    </#if>
}
