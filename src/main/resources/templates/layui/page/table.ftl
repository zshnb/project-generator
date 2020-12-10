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
<body>
<div class="layuimini-container">
    <div class="layuimini-main">
<#--        <fieldset class="table-search-fieldset">-->
<#--            <legend>搜索信息</legend>-->
<#--            <div style="margin: 10px 10px 10px 10px">-->
<#--                <form class="layui-form layui-form-pane" action="">-->
<#--                    <div class="layui-form-item">-->
<#--                        <div class="layui-inline">-->
<#--                            <label class="layui-form-label">用户姓名</label>-->
<#--                            <div class="layui-input-inline">-->
<#--                                <input type="text" name="username" autocomplete="off" class="layui-input">-->
<#--                            </div>-->
<#--                        </div>-->
<#--                        <div class="layui-inline">-->
<#--                            <label class="layui-form-label">用户性别</label>-->
<#--                            <div class="layui-input-inline">-->
<#--                                <input type="text" name="sex" autocomplete="off" class="layui-input">-->
<#--                            </div>-->
<#--                        </div>-->
<#--                        <div class="layui-inline">-->
<#--                            <label class="layui-form-label">用户城市</label>-->
<#--                            <div class="layui-input-inline">-->
<#--                                <input type="text" name="city" autocomplete="off" class="layui-input">-->
<#--                            </div>-->
<#--                        </div>-->
<#--                        <div class="layui-inline">-->
<#--                            <label class="layui-form-label">用户职业</label>-->
<#--                            <div class="layui-input-inline">-->
<#--                                <input type="text" name="classify" autocomplete="off" class="layui-input">-->
<#--                            </div>-->
<#--                        </div>-->
<#--                        <div class="layui-inline">-->
<#--                            <button type="submit" class="layui-btn layui-btn-primary"  lay-submit lay-filter="data-search-btn"><i class="layui-icon"></i> 搜 索</button>-->
<#--                        </div>-->
<#--                    </div>-->
<#--                </form>-->
<#--            </div>-->
<#--        </fieldset>-->

        <script type="text/html" id="toolbarDemo">
            <div class="layui-btn-container">
                <button class="layui-btn layui-btn-normal layui-btn-sm data-add-btn" lay-event="add"> 添加 </button>
            </div>
        </script>

        <table class="layui-hide" id="currentTableId" lay-filter="currentTableFilter"></table>

        <script type="text/html" id="currentTableBar">
            <a class="layui-btn layui-btn-normal layui-btn-xs data-count-edit" lay-event="edit">编辑</a>
            <a class="layui-btn layui-btn-xs layui-btn-danger data-count-delete" lay-event="delete">删除</a>
        </script>

    </div>
</div>
<script th:src="@{/lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
<script>
    layui.use(['form', 'table'], function () {
        var $ = layui.jquery,
            form = layui.form,
            table = layui.table

        table.render({
            elem: '#currentTableId',
            toolbar: '#toolbarDemo',
            defaultToolbar: [],
            url: '/${name}/list',
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
                    <#list table.fields as field>
                    {field: '${field.name}', width: 80, title: '${field.title}', sort: true},
                    </#list>
                    {title: '操作', minWidth: 150, toolbar: '#currentTableBar', align: 'center'}
                ]
            ],
            limits: [10, 15, 20, 25, 50, 100],
            limit: 15,
            page: true,
            skin: 'line'
        })

        // 监听搜索操作
        form.on('submit(data-search-btn)', function (data) {
            var result = JSON.stringify(data.field)
            layer.alert(result, {
                title: '最终的搜索信息'
            })

            //执行搜索重载
            table.reload('currentTableId', {
                page: {
                    curr: 1
                }
                , where: {
                    searchParams: result
                }
            }, 'data')

            return false
        })

        /**
         * toolbar监听事件
         */
        table.on('toolbar(currentTableFilter)', function (obj) {
            if (obj.event === 'add') {  // 监听添加操作
                var index = layer.open({
                    title: '添加',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: '/${name}/addPage',
                    end: function () {
                        table.reload('currentTableId')
                    }
                })
                $(window).on('resize', function () {
                    layer.full(index)
                })
            }
        })

        //监听表格复选框选择
        table.on('checkbox(currentTableFilter)', function (obj) {
            console.log(obj)
        })

        table.on('tool(currentTableFilter)', function (obj) {
            var data = obj.data
            if (obj.event === 'edit') {
                var index = layer.open({
                    title: '编辑',
                    type: 2,
                    shade: 0.2,
                    maxmin:true,
                    shadeClose: true,
                    area: ['100%', '100%'],
                    content: `/${name}/editPage/${r"${data.id}"}`,
                    end: function () {
                        table.reload('currentTableId')
                    }
                })
                $(window).on('resize', function () {
                    layer.full(index)
                })
                return false
            } else if (obj.event === 'delete') {
                layer.confirm('真的删除行么', function (index) {
                    var data = obj.data
                    $.ajax({
                        url: `/${name}/${r"${data.id}"}`,
                        type: 'delete'
                    })
                    layer.close(index)
                    table.reload('currentTableId')
                })
            }
        })
    })
</script>

</body>
</html>