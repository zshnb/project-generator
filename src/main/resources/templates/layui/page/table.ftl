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
                        <#list form.formItems as formItem>
                            <#if formItem.field.column.searchable>
                                <#switch formItem.class.simpleName>
                                    <#case "InputFormItem">
                                        <div class="layui-form-item">
                                            <div class="layui-inline">
                                                <label class="layui-form-label">${formItem.field.column.comment}</label>
                                                <div class="layui-input-inline">
                                                    <input type="text" name="${formItem.field.name}" autocomplete="off" class="layui-input">
                                                </div>
                                            </div>
                                        </div>
                                        <#break>
                                    <#case "SelectFormItem">
                                        <div class="layui-form-item">
                                            <label class="layui-form-label"></label>
                                            <div class="layui-input-block">
                                                <select name="${formItem.field.name}">
                                                    <option value="">请选择${formItem.field.column.comment}</option>
                                                    <#list formItem.options as option></#list>
                                                </select>
                                            </div>
                                        </div>
                                </#switch>
                            </#if>
                        </#list>
                        <div class="layui-form-item">
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
            <div class="layui-btn-container" th:if="${r"${#lists.contains(permissions, 'add')}"}">
                <button class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add"> 添加</button>
            </div>
        </script>

        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <a th:if="${r"${#lists.contains(permissions, 'detail')}"}" class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="detail">查看</a>
            <a th:if="${r"${#lists.contains(permissions, 'edit')}"}" class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>
            <a th:if="${r"${#lists.contains(permissions, 'delete')}"}" class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>
        </script>

    </div>
</div>
<script th:src="@{/lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
<script th:inline="javascript">
    layui.use(['form', 'table'], function () {
        let $ = layui.jquery,
            form = layui.form,
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

        table.render({
            elem: '#currentTableId',
            toolbar: '#toolbarDemo',
            defaultToolbar: [],
            url: '/${entity.name}/list',
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
                    <#list form.formItems as formItem>
                    <#assign fieldName>${formItem.field.name}</#assign>
                    <#assign comment>${formItem.field.column.comment}</#assign>
                    <#if formItem.field.column.enableFormItem && !formItem.field.column.associate??>
                        <#if formItem.class.simpleName == "FileFormItem">
                        { field: '${fieldName}', title: '${comment}', sort: true, templet: '<div><img src="{{d.${fieldName}}}"/></div>'},
                        <#else>
                        { field: '${fieldName}', title: '${comment}', sort: true },
                        </#if>
                    <#elseif formItem.field.column.associate??>
                        <#list formItem.field.column.associate.associateResultColumns as column>
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
                    let data = obj.data
                    $.ajax({
                        url: `/${entity.name}/${r"${data.id}"}`,
                        type: 'delete'
                    })
                    layer.close(index)
                    table.reload('currentTableId')
                })
                return false
            }
        })
    })
</script>
</body>
</html>