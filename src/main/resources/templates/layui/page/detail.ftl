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
    <#list form.formItems as formItem>
        <#assign comment>${formItem.field.column.comment}</#assign>
        <#assign formItemName>${formItem.field.name}</#assign>
        <#assign name>${entity.name}</#assign>
        <#if formItem.class.simpleName == "InputFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-input-block">
                    <input type="text" th:value="${r"${" + name + "." + formItemName + "}"}"
                           class="layui-input" readonly>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "DateTimeFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-input-block">
                    <input type="text" id="${formItemName}" th:value="${r"${" + name + "." + formItemName + "}"}"
                           value="" class="layui-input" readonly>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "SelectFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-input-block">
                    <select disabled>
                        <#list formItem.options as option>
                            <option value="${option.value}"
                                    th:selected="${r"${" + name + "." + formItemName + " == '" + option.value + "'}"}">${option.title}</option>
                        </#list>
                    </select>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "RadioFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-input-block">
                    <#list formItem.options as option>
                        <input type="radio" value="${option.value}" title="${option.name}" readonly
                               th:checked="${r"${" + name + "." + formItemName + " == '" + option.value + "'}"}">
                    </#list>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "TextAreaFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" readonly
                              th:text="${r"${" + name + "." + formItemName + "}"}">
                    </textarea>
                </div>
            </div>
        </#if>
    </#list>
</div>
<script th:src="@{/lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
</body>
</html>
<script>
    layui.use(['laydate'], function () {
        let laydate = layui.laydate

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
    })
</script>