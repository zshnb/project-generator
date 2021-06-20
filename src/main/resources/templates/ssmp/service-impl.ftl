package ${implPackageName};

<#assign name>${entity.name}</#assign>
<#assign className>${name?capFirst}</#assign>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.I${className}Service;
<#list dependencies as d>
import ${d}.*;
</#list>

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
            queryWrapper.eq(!exist.${getField}.equals(${name}.${getField}), "${field.column.name}", ${name}.${getField});
        </#list>
        ${className} mayExist = getOne(queryWrapper);
        if (mayExist != null) {
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
            ${className}Dto
        <#else>
            ${className}
        </#if>
    </#assign>
    @Override
    public ListResponse<<#compress>${returnClass}</#compress>> page(List${className}Request request<#if (entity.table.bindRoles?size > 0)>, User user</#if>) {
        <#if entity.table.searchable>
            <#if entity.table.associate>
                <#assign params>
                    <#list entity.fields?filter(f -> f.column.searchable) as field>
                        <#assign getField>request.get${field.name?capFirst}()</#assign>
                        ${getField}
                        <#if field_has_next>,</#if>
                    </#list>
                </#assign>
                IPage<<#compress>${returnClass}</#compress>> page = ${name}Mapper.findDtos(new Page<>(request.getPageNumber(), request.getPageSize()), ${params}<#if (entity.table.bindRoles?size > 0)>, user</#if>);
                return new ListResponse<>(page.getRecords(), page.getTotal());
            <#else>
            QueryWrapper<<#compress>${returnClass}</#compress>> queryWrapper = new QueryWrapper<>();
            <#list entity.fields as field>
                <#if field.column.searchable>
                    <#assign getField>request.get${field.name?capFirst}()</#assign>
                    <#if field.type == "String">
                        queryWrapper.like(!${getField}.isEmpty(), "${field.column.name}", ${getField});
                    <#elseIf field.type == "Integer">
                        queryWrapper.like(${getField} != 0, "${field.column.name}", ${getField});
                    </#if>
                </#if>
            </#list>
            <#if (entity.table.bindRoles?size > 0)>
                List<String> roles = new ArrayList<String>() {{
                <#list entity.table.bindRoles as role>
                    add("${role}");
                </#list>
                queryWrapper.eq(roles.contains(user.getRole()), "user_id", user.getId())
            }};
            </#if>
            IPage<<#compress>${returnClass}</#compress>> page = page(new Page<>(request.getPageNumber(), request.getPageSize()), queryWrapper);
            return new ListResponse<>(page.getRecords(), page.getTotal());
            </#if>
        <#else>
            <#if entity.table.associate>
                IPage<<#compress>${returnClass}</#compress>> page = ${name}Mapper.findDtos(new Page<>(request.getPageNumber(), request.getPageSize())<#if (entity.table.bindRoles?size > 0)>, user</#if>);
                return new ListResponse<>(page.getRecords(), page.getTotal());
            <#else>
                <#if (entity.table.bindRoles?size > 0)>
                List<String> roles = new ArrayList<String>() {{
                    <#list entity.table.bindRoles as role>
                        add("${role}");
                    </#list>
                }};
                </#if>
                IPage<<#compress>${returnClass}</#compress>> page = page(new Page<>(request.getPageNumber(), request.getPageSize())<#if (entity.table.bindRoles?size > 0)>,
                    new QueryWrapper<>().eq(roles.contains(user.getRole()), "user_id", user.getId())</#if>);
                return new ListResponse<>(page.getRecords(), page.getTotal());
            </#if>
        </#if>
    }
    @Override
    public ListResponse<${className}> listAll() {
        return new ListResponse<>(list(), 0L);
    }
    </#if>
}
