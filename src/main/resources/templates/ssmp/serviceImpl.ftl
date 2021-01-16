package ${implPackageName};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import ${packageName}.I${name?capFirst}Service;
<#list dependencies as d>
import ${d}.*;
</#list>

@Service
public class ${name?capFirst}ServiceImpl extends ServiceImpl<${name?capFirst}Mapper, ${name?capFirst}> implements I${name?capFirst}Service {
    @Autowired
    private ${name?capFirst}Mapper ${name}Mapper;

    @Override
    public ${name?capFirst} add(${name?capFirst} ${name}) {
        save(${name});
        return getById(${name}.getId());
    }

    @Override
    public ${name?capFirst} update(${name?capFirst} ${name}) {
        updateById(${name});
        return getById(${name}.getId());
    }

    @Override
    public ${name?capFirst} detail(int id) {
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
    @Override
    public ListResponse<${name?capFirst}> list(List${name?capFirst}Request request) {
<#--        <#if entity.fields.f-->
        IPage<${name?capFirst}> page = page(new Page<>(request.getPageNumber(), request.getPageSize()));
        return new ListResponse<>(page.getRecords(), page.getTotal());
    }
    </#if>

}
