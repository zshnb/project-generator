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
import org.springframework.web.servlet.ModelAndView;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;
<#function camelize(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncapFirst>
</#function>
<#assign className>${entity.name?capFirst}</#assign>
<#assign comment>${entity.table.comment}</#assign>
<#assign name>${entity.name}</#assign>
@Controller
@RequestMapping("/${name}")
public class ${className}Controller {
    <#assign service>${name}Service</#assign>
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
    /**
        添加${comment}
    */
    @PostMapping("/add")
    @ResponseBody
    public Response<${className}> add(@RequestBody ${className} request) {
        ${className} ${name} = ${service}.add(request);
        return Response.ok(${name});
    }

    /**
        更新${comment}
    */
    @PutMapping("/update")
    @ResponseBody
    public Response<${className}> update(@RequestBody ${className} old) {
        ${className} ${name} = ${service}.update(old);
        return Response.ok(${name});
    }

    /**
        删除${comment}
    */
    @DeleteMapping("/{id}")
    @ResponseBody
    public Response<String> delete(@PathVariable int id) {
        ${service}.delete(id);
        return Response.ok();
    }

    /**
        分页列出${comment}
    */
    <#assign returnClass>
        <#if entity.table.associate>
            ${name?capFirst}Dto<#t>
        <#else>
            ${className}<#t>
        </#if>
    </#assign>
    @PostMapping("/page")
    @ResponseBody
    public ListResponse<${returnClass}> page(@RequestBody List${className}Request request, HttpSession session) {
        User user = (User) session.getAttribute("user");
        return ${service}.page(request<#if (entity.table.bindRoles?size > 0)>, user</#if>);
    }

    <#list operations?filter(it -> it.type == "AJAX") as operation>
        <#assign methodName = "${camelize(operation.value?replace('-', '_'))}"/>
        @${operation.detail.httpMethod?lowerCase?capFirst}Mapping("/${operation.value}<#if operation.detail.pathVariable>/{id}</#if>")
        @ResponseBody
        public Response<String> ${methodName}(<#if operation.detail.pathVariable>@PathVariable int id</#if>) {
            ${service}.${methodName}(<#if operation.detail.pathVariable>id</#if>);
            return Response.ok();
        }
    </#list>
    /**
        列出${comment}
    */
    @GetMapping("/list")
    @ResponseBody
    public ListResponse<${name?capFirst}> list() {
        return ${service}.listAll();
    }

    /**
        跳转${comment}表格页面
    */
    @GetMapping("/tablePage")
    public ModelAndView tablePage(HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<Permission> permissions = permissionService.list(new QueryWrapper<Permission>()
            .eq("role", user.getRole())
            .eq("model", "${name}"));
        ModelAndView modelAndView = new ModelAndView("page/${name}/table");
        modelAndView.addObject("permissions", permissions.stream().map(Permission::getOperation).collect(Collectors.toList()));
        return modelAndView;
    }

    /**
        跳转${comment}添加页面
    */
    @GetMapping("/addPage")
    public ModelAndView addPage () {
        return new ModelAndView("page/${name}/add");
    }

    /**
        跳转${comment}编辑页面
    */
    @GetMapping("/editPage/{id}")
    public ModelAndView editPage(@PathVariable int id) {
        ${className} ${name} = ${service}.getById(id);
        ModelAndView modelAndView = new ModelAndView("page/${name}/edit");
        modelAndView.addObject("${name}", ${name});
        return modelAndView;
    }

    /**
        跳转${comment}详情页面
    */
    @GetMapping("/detailPage/{id}")
    public ModelAndView detailPage(@PathVariable int id) {
        ${className} ${name} = ${service}.getById(id);
        ModelAndView modelAndView = new ModelAndView("page/${name}/detail");
        modelAndView.addObject("${name}", ${name});
        return modelAndView;
    }

    <#list operations?filter(it -> it.type == "NEW_PAGE") as operation>
    @GetMapping("/${operation.value}<#if operation.detail.pathVariable>/{id}</#if>")
    public ModelAndView ${camelize(operation.value?replace('-', '_'))}(<#if operation.detail.pathVariable>@PathVariable int id</#if>) {
        return new ModelAndView("page/${name}");
    }
    </#list>
    </#if>
}
