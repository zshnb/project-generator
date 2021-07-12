package ${packageName};

<#assign name>${entity.name}</#assign>
<#assign className>${entity.name?capFirst}</#assign>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import ${servicePackageName}.I${className}Service;
<#list dependencies as d>
import ${d}.*;
</#list>
<#function literalize(str)>
    <#if config.database == "MYSQL">
        <#return '`${str}`'>
    <#elseIf config.database == "SQLSERVER">
        <#return '[${str}]'>
    </#if>
</#function>
@Service
public class ${className}ServiceImpl extends ServiceImpl<${className}Mapper, ${className}> implements I${className}Service {
    @Autowired
    private ${className}Mapper ${name}Mapper;

    @Override
    public ${className} add(${className} ${name}) {
        <#if (entity.fields?filter(f -> !f.column.repeatable)?size > 0)>
        QueryWrapper<${className}> queryWrapper = new QueryWrapper<>();
        <#list entity.fields?filter(f -> !f.column.repeatable) as field>
            <#assign getField>${name}.get${field.name?capFirst}()</#assign>
            queryWrapper.eq("${field.column.name}", ${getField});
        </#list>
        ${className} mayExist = getOne(queryWrapper);
        if (mayExist != null) {
            throw new InvalidArgumentException("记录已重复存在");
        }
        </#if>
        save(${name});
        return getById(${name}.getId());
    }

    @Override
    public ${className} update(${className} ${name}) {
        <#if (entity.fields?filter(f -> !f.column.repeatable)?size > 0)>
        ${className} exist = getById(${name}.getId());
        QueryWrapper<${className}> queryWrapper = new QueryWrapper<>();
        <#list entity.fields?filter(f -> !f.column.repeatable) as field>
            <#assign getField>get${field.name?capFirst}()</#assign>
            queryWrapper.eq("${field.column.name}", ${name}.${getField});
        </#list>
        ${className} mayExist = getOne(queryWrapper);
        if (mayExist != null && !exist.getId().equals(mayExist.getId())) {
            throw new InvalidArgumentException("记录已重复存在");
        }
        </#if>
        updateById(${name});
        return getById(${name}.getId());
    }

    @Override
    public ${className} detail(int id) {
        return getById(id);
    }

    @Override
    public void delete(int id) {
        getBaseMapper().deleteById(id);
    }

    <#if name == "menu">
    @Override
    public ListResponse<MenuDto> list(String role) {
        List<Menu> menus = list(new QueryWrapper<Menu>()
            .eq("role", role)
            .eq("parent_id", 0));
        List<MenuDto> menuDtos = menus.stream().map(it -> {
            MenuDto menuDto = new MenuDto();
            menuDto.setId(it.getId());
            menuDto.setName(it.getName());
            menuDto.setHref(it.getHref());
            menuDto.setIcon(it.getIcon());
            menuDto.setRole(it.getRole());
            setChildMenu(menuDto);
            return menuDto;
        }).collect(Collectors.toList());
        return new ListResponse<>(menuDtos, menuDtos.size());
    }
    private void setChildMenu(MenuDto menu) {
        List<MenuDto> child = list(new QueryWrapper<Menu>()
            .eq("parent_id", menu.getId()))
            .stream()
            .map(it -> {
        MenuDto menuDto = new MenuDto();
        menuDto.setId(it.getId());
        menuDto.setName(it.getName());
        menuDto.setHref(it.getHref());
        menuDto.setIcon(it.getIcon());
        menuDto.setRole(it.getRole());
        return menuDto;
        }).collect(Collectors.toList());
        menu.setChild(child);

        menu.getChild().forEach(this::setChildMenu);
    }
    <#else>
    <#assign returnClass>
        <#if entity.table.associate>
            ${className}Dto<#t>
        <#else>
            ${className}<#t>
        </#if>
    </#assign>
    <#assign hasBindRoles = "${(entity.table.bindRoles?size > 0)?c}"/>
    @Override
    public ListResponse<${returnClass}> page(List${className}Request request<#if hasBindRoles == "true">, User user</#if>) {
        <#assign pageParam>
            <#if config.database == "MYSQL">
                new Page<>(request.getPageNumber(), request.getPageSize())<#t>
            <#elseIf config.database == "SQLSERVER">
                new Page<>(request.getPageNumber(), request.getPageSize(), false)<#t>
            </#if>
        </#assign>
        <#assign returnTotal>
            <#if config.database == "MYSQL">
                page.getTotal()<#t>
            <#elseIf config.database == "SQLSERVER">
                ${name}Mapper.count(<#if entity.table.searchable>request</#if><#if hasBindRoles == "true">, user</#if>)<#t>
            </#if>
        </#assign>
        <#if entity.table.searchable>
            <#if entity.table.associate>
                IPage<${returnClass}> page = ${name}Mapper.findDtos(${pageParam}<#if entity.table.searchable>, request</#if><#if hasBindRoles == "true">, user</#if>);
                return new ListResponse<>(page.getRecords(), ${returnTotal});
            <#else>
            QueryWrapper<${returnClass}> queryWrapper = new QueryWrapper<>();
            <#list entity.fields as field>
                <#if field.column.searchable>
                    <#assign getField>request.get${field.name?capFirst}()</#assign>
                    <#if field.type == "String">
                        queryWrapper.like(!${getField}.isEmpty(), "${literalize(field.column.name)}", ${getField});
                    <#elseIf field.type == "Integer">
                        queryWrapper.eq(${getField} != 0, "${literalize(field.column.name)}", ${getField});
                    </#if>
                </#if>
            </#list>
            <#if hasBindRoles == "true">
                List<String> roles = new ArrayList<String>() {{
                <#list entity.table.bindRoles as role>
                    add("${role}");
                </#list>
                queryWrapper.eq(roles.contains(user.getRole()), "user_id", user.getId())
            }};
            </#if>
            IPage<${returnClass}> page = page(${pageParam}, queryWrapper);
            return new ListResponse<>(page.getRecords(), ${returnTotal});
            </#if>
        <#else>
            <#if entity.table.associate>
                IPage<${returnClass}> page = ${name}Mapper.findDtos(${pageParam}<#if hasBindRoles == "true">, user</#if>);
                return new ListResponse<>(page.getRecords(), ${returnTotal});
            <#else>
                <#if hasBindRoles == "true">
                List<String> roles = new ArrayList<String>() {{
                    <#list entity.table.bindRoles as role>
                        add("${role}");
                    </#list>
                }};
                </#if>
                IPage<${returnClass}> page = page(${pageParam}<#if hasBindRoles == "true">,
                    new QueryWrapper<>().eq(roles.contains(user.getRole()), "user_id", user.getId())</#if>);
                return new ListResponse<>(page.getRecords(), ${returnTotal});
            </#if>
        </#if>
    }
    @Override
    public ListResponse<${className}> listAll() {
        return new ListResponse<>(list(), 0L);
    }
    </#if>
}
