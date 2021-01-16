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
    <style>
        body {
            background-color: #ffffff;
        }
    </style>
</head>
<body>
<div class="layui-form layuimini-form">
    <input type="hidden" name="id" th:value="${r"${" + entity.name + ".id}"}">
    <#list form.formItems as formItem>
        <#assign comment>${formItem.field.column.comment}</#assign>
        <#assign formItemName>${formItem.field.name}</#assign>
        <#assign name>${entity.name}</#assign>
        <#if formItem.class.simpleName == "InputFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <input type="text" name="${formItemName}" th:value="${r"${" + name + "." + formItemName + "}"}"
                            <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                           placeholder="请输入${comment}" class="layui-input">
                </div>
            </div>
        <#elseif formItem.class.simpleName == "DateTimeFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <input type="text" name="${formItemName}" id="${formItemName}"
                           <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                           placeholder="请输入${comment}" value="" class="layui-input">
                </div>
            </div>
        <#elseif formItem.class.simpleName == "SelectFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <select name="${formItemName}" <#if formItem.require>lay-verify="required"
                            lay-reqtext="${comment}不能为空"</#if>>
                        <option value="">请选择${comment}</option>
                        <#list formItem.options as option>
                            <option value="${option.value}"
                                    th:selected="${r"${" + name + "." + formItemName + " == " + option.value + "}"}">${option.title}</option>
                        </#list>
                    </select>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "RadioFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <#list formItem.options as option>
                        <input type="radio" name="${formItemName}" value="${option.value}" title="${option.name}"
                               th:checked="${r"${" + name + "." + formItemName + " == " + option.value + "}"}"
                               <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>>>
                    </#list>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "TextAreaFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" name="${formItemName}"
                              th:text="${r"${" + name + "." + formItemName + "}"}"
                              <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>>
                    </textarea>
                </div>
            </div>
        </#if>
    </#list>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn layui-btn-normal" lay-submit lay-filter="saveBtn">确认修改</button>
        </div>
    </div>
</div>
<script th:src="@{/lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
<script th:inline="javascript">
    layui.use(['form', 'laydate'], function () {
        let form = layui.form,
            $ = layui.$,
            laydate = layui.laydate

        <#list form.formItems as formItem>
        <#if formItem.class.simpleName == "DateTimeFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'datetime',
            format: 'yyyy-MM-dd HH:mm:ss',
            value: ${r"[[${#temporals.format(" + entity.name + "." + formItem.field.name + ", 'yyyy-MM-dd HH:mm:ss')}]]"}
        })
        </#if>
        </#list>

        //监听提交
        form.on('submit(saveBtn)', function (data) {
            $.ajax({
                url: '/${entity.name}/update',
                type: 'put',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                success: function () {
                    let iframeIndex = parent.layer.getFrameIndex(window.name)
                    parent.layer.close(iframeIndex)
                }
            })
            return false
        })
    })
</script>
</body>
</html>