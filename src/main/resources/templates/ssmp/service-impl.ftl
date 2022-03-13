package ${packageName};

<#assign name>${entity.name}</#assign>
<#assign className>${entity.name?capFirst}</#assign>
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
<#if (tables?filter(it -> it.bindUser??)?size > 0)>
    import org.springframework.transaction.annotation.Transactional;
</#if>
<#if entity.table.associate>
    import ${dtoPackageName}.*;
</#if>
import java.util.List;
import java.util.stream.Collectors;
import ${servicePackageName}.I${className}Service;
<#list dependencies as d>
import ${d}.*;
</#list>
<#function camelize(s)>
    <#return s
    ?replace('(^_+)|(_+$)', '', 'r')
    ?replace('\\_+(\\w)?', ' $1', 'r')
    ?replace('([A-Z])', ' $1', 'r')
    ?capitalize
    ?replace(' ' , '')
    ?uncapFirst>
</#function>
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

    <#if name != "user" && (tables?filter(it -> it.bindUser?? && it.name == name)?size > 0)>
        @Autowired
        private UserMapper userMapper;
    </#if>
    <#if name == "user" && (tables?filter(it -> it.bindUser??)?size > 0)>
        <#list tables?filter(it -> it.bindUser??) as table>
            @Autowired
            private ${camelize(table.name)?capFirst}Mapper ${camelize(table.name)}Mapper;
        </#list>
    </#if>
    <#if (tables?filter(it -> it.bindUser??)?size > 0)>
    @Transactional
    </#if>
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
        <#if name == "user">
            save(${name});
            <#list tables?filter(it -> it.bindUser??) as table>
                <#assign innerTableName = "${camelize(table.name)}"/>
                if (user.getRole().equals("${innerTableName}")) {
                    ${innerTableName?capFirst} ${innerTableName} = new ${innerTableName?capFirst}();
                    ${innerTableName}.set${table.bindUser?capFirst}(user.getUsername());
                    ${innerTableName}.setUserId(user.getId());
                    ${innerTableName}Mapper.insert(${innerTableName});
                }
            </#list>
        <#elseIf entity.table.bindUser??>
            <#assign innerTableName = "${entity.name}"/>
            User user = new User();
            user.setUsername(${innerTableName}.get${entity.table.bindUser?capFirst}());
            user.setPassword("123456");
            user.setRole("${innerTableName}");
            userMapper.insert(user);
            ${innerTableName}.setUserId(user.getId());
            save(${name});
        <#else>
            save(${name});
        </#if>
        return getById(${name}.getId());
    }

    @Override
    public ${className} update(${className} ${name}) {
        <#if (entity.fields?filter(f -> !f.column.repeatable)?size > 0)>
        QueryWrapper<${className}> queryWrapper = new QueryWrapper<>();
        <#list entity.fields?filter(f -> !f.column.repeatable) as field>
            <#assign getField>get${field.name?capFirst}()</#assign>
            queryWrapper.eq("${field.column.name}", ${name}.${getField});
        </#list>
        ${className} mayExist = getOne(queryWrapper);
        if (mayExist != null && !${name}.getId().equals(mayExist.getId())) {
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
    public ListResponse<Menu> list(String role) {
        List<Menu> menus = list(new QueryWrapper<Menu>()
            .eq("role", role));
        return new ListResponse<>(menus, menus.size());
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
                    <#elseIf field.type == "LocalDate" || field.type == "LocalDateTime">
                        queryWrapper.eq(${getField} != null, "${literalize(field.column.name)}", ${getField});
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
    <#list operations as operation>
    @Override
    public Response<String> ${camelize(operation.value?replace('-', '_'))}(<#if operation.detail.pathVariable>int id</#if>) {
        return Response.ok();
    }
    </#list>
}
