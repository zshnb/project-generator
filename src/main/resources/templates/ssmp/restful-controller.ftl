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
import org.springframework.web.bind.annotation.RestController;

<#assign comment>entity.table.comment</#assign>
@RestController
@RequestMapping("/${name}")
public class ${name?capFirst}Controller {
    <#assign service>${name}Service</#assign>
    <#assign className>${name?capFirst}</#assign>

    @Autowired
    private ${className}ServiceImpl ${service};

    <#if name == "menu">
    @PostMapping("/list")
    public ListResponse<MenuDto> list(@RequestBody ListMenuRequest request) {
        return menuService.list(request);
    }
    <#else>
    /**
        添加${comment}
    */
    @PostMapping("/add")
    public Response<${className}> add(@RequestBody ${className} request) {
        ${className} ${name} = ${service}.add(request);
        return Response.ok(${name});
    }

    /**
        更新${comment}
    */
    @PutMapping("/update")
    public Response<${className}> update(@RequestBody ${className} old) {
        ${className} ${name} = ${service}.update(old);
        return Response.ok(${name});
    }

    /**
        获取${comment}
    */
    @GetMapping("/{id}")
    public Response<${className}> detail(@PathVariable int id) {
        return Response.ok(${service}.detail(id));
    }

    /**
        删除${comment}
    */
    @DeleteMapping("/{id}")
    public Response<String> delete(@PathVariable int id) {
        ${service}.delete(id);
        return Response.ok();
    }

    /**
        列出${comment}
    */
    @PostMapping("/list")
    public ListResponse<${className}> list(@RequestBody List${className}Request request) {
        return ${service}.list(request);
    }
    </#if>
}
