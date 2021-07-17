<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>layui</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" th:href="@{/lib/layui-v2.5.5/css/layui.css}" media="all">
    <link rel="stylesheet" th:href="@{/css/public.css}" media="all">
</head>
<style>
    .layui-table-cell {
        height: auto;
    }
</style>
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
        <#if entity.table.searchable>
            <fieldset class="table-search-fieldset">
                <legend>搜索信息</legend>
                <div style="margin: 10px 10px 10px 10px">
                    <form class="layui-form layui-form-pane" action="">
                        <div class="layui-form-item">
                            <#list form.items?filter(it -> it.field.column.searchable) as formItem>
                                <#assign label>${formItem.label}</#assign>
                                <#if formItem.field.column.searchable>
                                    <#switch formItem.class.simpleName>
                                        <#case "InputFormItem">
                                            <div class="layui-inline">
                                                <label class="layui-form-label">${label}</label>
                                                <div class="layui-input-inline">
                                                    <input type="text" name="${formItem.field.name}" autocomplete="off" class="layui-input">
                                                </div>
                                            </div>
                                            <#break>
                                        <#case "SelectFormItem">
                                            <div class="layui-inline">
                                                <label class="layui-form-label">${label}</label>
                                                <div class="layui-input-inline">
                                                    <select name="${formItem.field.name}">
                                                        <option value="">请选择${label}</option>
                                                        <#if !formItem.field.column.associate??>
                                                            <#list formItem.options as option>
                                                                <option value="${option.value}">${option.title}</option>
                                                            </#list>
                                                        </#if>
                                                    </select>
                                                </div>
                                            </div>
                                            <#break>
                                        <#case "DateFormItem">
                                        <#case "DateTimeFormItem">
                                            <div class="layui-inline">
                                                <label class="layui-form-label">${label}</label>
                                                <div class="layui-input-inline">
                                                    <input type="text" name="${formItem.field.name}" id="${formItem.field.name}"
                                                           placeholder="请输入${label}" value="" class="layui-input"/>
                                                </div>
                                            </div>
                                            <#break>
                                    </#switch>
                                </#if>
                            </#list>
                            <div class="layui-inline">
                                <button type="submit" class="layui-btn layui-btn-primary" lay-submit
                                        lay-filter="data-search-btn"><i class="layui-icon"></i> 搜 索
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </fieldset>
        </#if>

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <#list table.operations?filter(o -> o.position == "toolbar") as operation>
                    <button th:if="${r"${#lists.contains(permissions, '" + operation.value + "')}"}"
                            class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="${operation.value}">${operation.description}</button>
                </#list>
            </div>
        </script>

        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <#list table.operations?filter(o -> o.position == "toolColumn") as operation>
                <a th:if="${r"${#lists.contains(permissions, '" + operation.value + "')}"}"
                   class="layui-btn layui-btn-xs layui-btn-normal data-count-delete" lay-event="${operation.value}">${operation.description}</a>
            </#list>
        </script>

        <#list table.fields as tableField>
            <#if tableField.formItemClassName == "com.zshnb.projectgenerator.generator.entity.FileFormItem">
                <script type="text/html" id="${tableField.field.name}">
                    {{#
                        let fileName = d.${tableField.field.name}
                    }}
                    <a class="layui-btn layui-btn-xs" href="/download?fileName={{fileName}}" download="{{fileName}}">下载${tableField.title}</a>
                </script>
            <#elseif tableField.mappings??>
                <script type="text/html" id="${tableField.field.name}">
                    <#list tableField.mappings as mapping>
                        <#if tableField.field.type == "Integer" || tableField.field.type == "Double">
                            {{# if(d.${tableField.field.name} === ${mapping.source}){ }}
                        <#elseif tableField.field.type == "String">
                            {{# if(d.${tableField.field.name} === '${mapping.source}'){ }}
                        </#if>
                        <span>${mapping.target}</span>
                        {{# } }}
                    </#list>
                </script>
            </#if>
        </#list>
    </div>
</div>
<script th:src="@{/lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
<script th:inline="javascript">
    layui.use(['form', 'table', 'laydate'], function () {
        let $ = layui.jquery,
            form = layui.form,
            laydate = layui.laydate,
            table = layui.table
        <#function camelize(s)>
        <#return s
            ?replace('(^_+)|(_+$)', '', 'r')
            ?replace('\\_+(\\w)?', ' $1', 'r')
            ?replace('([A-Z])', ' $1', 'r')
            ?capitalize
            ?replace(' ' , '')
            ?uncap_first>
        </#function>
        <#list form.items as formItem>
        <#if formItem.field.column.associate?? && formItem.field.column.searchable>
        $.ajax({
            type: 'get',
            url: '/${camelize(formItem.field.column.associate.targetTableName)}/list',
            success: function (data) {
                data.data.forEach(it => {
                    $('select[name=${camelize(formItem.field.column.name)}]').append(`<option value="${r"${it.id}"}">${r"${it." + formItem.field.column.associate.formItemColumnName + "}"}</option>`)
                })
                form.render('select')
            }
        })
        <#elseif formItem.class.simpleName == "DateTimeFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'datetime',
            trigger: 'click'
        })
        <#elseif formItem.class.simpleName == "DateFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'date',
            trigger: 'click'
        })
        </#if>
        </#list>

        table.render({
            elem: '#currentTableId',
            toolbar: '#toolbarDemo',
            defaultToolbar: [],
            url: '/${entity.name}/page',
            method: 'post',
            contentType: 'application/json',
            parseData: function (res) {
                return {
                    'code': 0,
                    'count': res.total,
                    'data': res.data
                }
            },
            request: {
                pageName: 'pageNumber',
                limitName: 'pageSize'
            },
            cols: [
                [
                    <#list table.fields as tableField>
                        <#assign fieldName>${tableField.field.name}</#assign>
                        <#assign title>${tableField.title}</#assign>
                        <#if tableField.field.column.enableTableField && !tableField.field.column.associate??>
                            <#if tableField.formItemClassName == "com.zshnb.projectgenerator.generator.entity.ImageFormItem">
                            { field: '${fieldName}', title: '${title}', sort: true, templet: '<div><img src="/download?fileName={{d.${fieldName}}}"/></div>'},
                            <#elseif tableField.formItemClassName == "com.zshnb.projectgenerator.generator.entity.FileFormItem">
                            { field: '${fieldName}', title: '${title}', sort: true, templet: '#${tableField.field.name}'},
                            <#elseif tableField.mappings??>
                            { field: '${fieldName}', title: '${title}', sort: true, templet: '#${tableField.field.name}' },
                            <#else>
                            { field: '${fieldName}', title: '${title}', sort: true },
                            </#if>
                        <#elseif tableField.field.column.associate??>
                            <#list tableField.field.column.associate.associateResultColumns as column>
                                { field: '${column.aliasColumnName}', title: '${column.tableFieldTitle}', sort: true },
                            </#list>
                        </#if>
                    </#list>
                    { title: '操作', toolbar: '#currentTableBar', align: 'center' }
                ]
            ],
            limits: [10, 15, 20, 25, 50, 100],
            limit: 15,
            page: true,
            skin: 'line'
        })

        // 监听搜索操作
        form.on('submit(data-search-btn)', function (data) {
            //执行搜索重载
            table.reload('currentTableId', {
                page: {
                    curr: 1
                }, where: {
                    <#list entity.fields as field>
                        <#if field.column.searchable>
                            ${field.name}: data.field.${field.name}<#if field_has_next>,</#if>
                        </#if>
                    </#list>
                }
            }, 'data')

            return false
        })

        /**
         * toolbar监听事件
         */
        table.on('toolbar(currentTableFilter)', function (obj) {
            if (obj.event === 'add') {  // 监听添加操作
                let index = layer.open({
                    title: '添加',
                    type: 2,
                    shade: 0.2,
                    maxmin: true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: '/${entity.name}/addPage',
                    end: function () {
                        table.reload('currentTableId')
                    }
                })
                $(window).on('resize', function () {
                    layer.full(index)
                })
            }
        })

        table.on('tool(currentTableFilter)', function (obj) {
            let data = obj.data
            if (obj.event === 'edit') {
                let index = layer.open({
                    title: '编辑',
                    type: 2,
                    shade: 0.2,
                    maxmin: true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: `/${entity.name}/editPage/${r"${data.id}"}`,
                    end: function () {
                        table.reload('currentTableId')
                    }
                })
                $(window).on('resize', function () {
                    layer.full(index)
                })
                return false
            } else if (obj.event === 'detail') {
                let index = layer.open({
                    title: '查看',
                    type: 2,
                    shade: 0.2,
                    maxmin: true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: `/${entity.name}/detailPage/${r"${data.id}"}`,
                })
                $(window).on('resize', function () {
                    layer.full(index)
                })
                return false
            } else if (obj.event === 'delete') {
                layer.confirm('真的删除行么', function (index) {
                    $.ajax({
                        url: `/${entity.name}/${r"${data.id}"}`,
                        type: 'delete',
                        success: function () {
                            layer.close(index)
                            table.reload('currentTableId')
                        }
                    })
                })
                return false
            }
        })
    })
</script>
</body>
</html>