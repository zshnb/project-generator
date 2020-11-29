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
    <input type="hidden" name="id" th:value="${r"${" + name + ".id}"}">
    <#list form.formItems as formItem>
        <#if formItem.class.simpleName == "InputFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label required">${formItem.comment}</label>
                <div class="layui-input-block">
                    <input type="text" name="${formItem.name}" th:value="${r"${" + name + "." + formItem.name + "}"}" <#if formItem.require>lay-verify="required" lay-reqtext="${formItem.comment}不能为空"</#if> placeholder="请输入${formItem.comment}" th:value="${r"${" + name + "." + formItem.name + "}"}" class="layui-input">
                </div>
            </div>
        <#elseif formItem.class.simpleName == "SelectFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${formItem.comment}</label>
                <div class="layui-input-block">
                    <select name="${formItem.name}" <#if formItem.require>lay-verify="required" lay-reqtext="${formItem.comment}不能为空"</#if>>
                        <option value=""></option>
                        <#list formItem.options as option>
                            <option value="${option.value}" th:selected="${r"${" + name + "." + formItem.name + " == " + option.value + "}"}">${option.title}</option>
                        </#list>
                    </select>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "RadioFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${formItem.comment}</label>
                <div class="layui-input-block">
                    <#list formItem.options as option>
                        <input type="radio" name="${formItem.name}" value="${option.value}" title="${option.name}" th:checked="${r"${" + name + "." + formItem.name + " == " + option.value + "}"}" <#if formItem.require>lay-verify="required" lay-reqtext="${formItem.comment}不能为空"</#if>>>
                    </#list>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "TextAreaFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">${formItem.comment}</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" name="${formItem.name}" th:text="${r"${" + name + "." + formItem.name + "}"}" <#if formItem.require>lay-verify="required" lay-reqtext="${formItem.comment}不能为空"</#if>>
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
<script th:src="@{lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
<script>
    layui.use(['form'], function () {
        var form = layui.form,
            $ = layui.$;

        //监听提交
        form.on('submit(saveBtn)', function (data) {
            $.ajax({
                url: '/${name}/update',
                type: 'put',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                success: function () {
                    var iframeIndex = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(iframeIndex);
                }
            });

            return false;
        });

    });
</script>
</body>
</html>