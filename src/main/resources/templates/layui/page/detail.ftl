<#if projectType == "ssm">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
</#if>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <title>layui</title>
    <meta name="renderer" content="webkit">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <#if projectType == "ssm">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/static/lib/layui/css/layui.css" media="all">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/static/css/public.css" media="all">
    <#else>
        <link rel="stylesheet" th:href="@{/lib/layui/css/layui.css}" media="all">
        <link rel="stylesheet" th:href="@{/css/public.css}" media="all">
    </#if>
    <style>
        body {
            background-color: #ffffff;
        }
    </style>
</head>
<body>
<div class="layui-form layuimini-form">
    <#function camelize(s)>
        <#return s
        ?replace('(^_+)|(_+$)', '', 'r')
        ?replace('\\_+(\\w)?', ' $1', 'r')
        ?replace('([A-Z])', ' $1', 'r')
        ?capitalize
        ?replace(' ' , '')
        ?uncap_first>
    </#function>
    <#list page.form.items?filter(it -> it.field.column.enableFormItem) as formItem>
        <#assign label>${formItem.label}</#assign>
        <#assign formItemName>${formItem.field.name}</#assign>
        <#assign name>${page.entity.name}</#assign>
        <#if formItem.class.simpleName == "InputFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${label}</label>
                <div class="layui-input-block">
                    <input type="text" <#if projectType != "ssm">th:</#if>value="${r"${" + name + "." + formItemName + "}"}"
                           class="layui-input" readonly>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "PasswordFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${label}</label>
                <div class="layui-input-block">
                    <input type="password" name="${formItemName}"<#if projectType != "ssm">th:</#if>value="${r"${" + name + "." + formItemName + "}"}"
                           <#if formItem.require>lay-verify="required" lay-reqtext="${label}不能为空"</#if>
                           placeholder="请输入${label}" value="" class="layui-input" readonly>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "DateTimeFormItem" || formItem.class.simpleName == "DateFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${label}</label>
                <div class="layui-input-block">
                    <input type="text" id="${formItemName}" value="" class="layui-input" readonly>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "SelectFormItem">
            <#if formItem.field.column.associate??>
                <div class="layui-form-item">
                    <label class="layui-form-label <#if formItem.require>required</#if>">${label}</label>
                    <div class="layui-input-block">
                        <select name="${camelize(formItem.field.column.name)}" disabled
                                <#if formItem.require>lay-verify="required" lay-reqtext="${label}不能为空"</#if>>
                            <option value="">请选择${label}</option>
                        </select>
                    </div>
                </div>
            <#else>
                <div class="layui-form-item">
                    <label class="layui-form-label">${label}</label>
                    <div class="layui-input-block">
                        <select name="${formItemName}" disabled>
                            <#if projectType == "ssm">
                                <#list formItem.options as option>
                                    <option value="${option.value}"
                                            ${r"${" + name + "." + formItemName + " == '" + option.value + "' ? 'selected' : ''}"}>${option.title}</option>
                                </#list>
                            <#else>
                                <#list formItem.options as option>
                                    <option value="${option.value}"
                                            th:selected="${r"${" + name + "." + formItemName + " == '" + option.value + "'}"}">${option.title}</option>
                                </#list>
                            </#if>
                        </select>
                    </div>
                </div>
            </#if>
        <#elseif formItem.class.simpleName == "RadioFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label">${label}</label>
                <div class="layui-input-block">
                    <#if projectType == "ssm">
                        <#list formItem.options as option>
                            <input type="radio" value="${option.value}" title="${option.title}" readonly
                                    ${r"${" + name + "." + formItemName + " == '" + option.value + "' ? 'checked' : ''}"}/>
                        </#list>
                    <#else>
                        <#list formItem.options as option>
                            <input type="radio" value="${option.value}" title="${option.title}" readonly
                                   th:checked="${r"${" + name + "." + formItemName + " == '" + option.value + "'}"}">
                        </#list>
                    </#if>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "TextAreaFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">${label}</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" readonly
                              <#if projectType != "ssm">th:text="${r"${" + name + "." + formItemName + "}"}"</#if>>
                        <#if projectType == "ssm">
                            ${r"${" + name + "." + formItemName + "}"}
                        </#if>
                    </textarea>
                </div>
            </div>
        </#if>
    </#list>
</div>
</body>
</html>
<#if projectType == "ssm">
    <script src="<%=request.getContextPath() %>/static/lib/layui/layui.js" charset="utf-8"></script>
<#else>
    <script th:src="@{/lib/layui/layui.js}" charset="utf-8"></script>
</#if>
<script <#if projectType != "ssm">th:inline="javascript"</#if>>
    layui.use(['laydate', 'form'], function () {
        let laydate = layui.laydate,
            form = layui.form,
            $ = layui.$
        <#list page.form.items?filter(it -> it.field.column.enableFormItem) as formItem>
        <#if formItem.class.simpleName == "DateTimeFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'datetime',
            <#if projectType == "ssm">
            value: ${r"'${" + page.entity.name + "." + formItem.field.name + "}'"},
            <#else>
            value: ${r"[[${#temporals.format(" + page.entity.name + "." + formItem.field.name + ", 'yyyy-MM-dd HH:mm:ss')}]]"},
            </#if>
        })
        <#elseif formItem.class.simpleName == "DateFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'date',
            <#if projectType == "ssm">
            value: ${r"${" + page.entity.name + "." + formItem.field.name + "}"},
            <#else>
            value: ${r"[[${#temporals.format(" + page.entity.name + "." + formItem.field.name + ", 'yyyy-MM-dd')}]]"},
            </#if>
        })
        <#elseif formItem.field.column.associate??>
        $.ajax({
            type: 'get',
            url: '/${camelize(formItem.field.column.associate.targetTableName)}/list',
            success: function (data) {
                data.data.forEach(it => {
                    <#if projectType == "ssm">
                    if (it.id === ${r"${" + page.entity.name + "." + camelize(formItem.field.column.name) + "}"}) {
                        $('select[name=${camelize(formItem.field.column.name)}]').append(`<option value="${r"\${it.id}"}" selected>${r"\${it." + formItem.field.column.associate.formItemColumnName + "}"}</option>`)
                    } else {
                        $('select[name=${camelize(formItem.field.column.name)}]').append(`<option value="${r"\${it.id}"}">${r"\${it." + formItem.field.column.associate.formItemColumnName + "}"}</option>`)
                    }
                    <#else>
                    if (it.id === ${r"[[${" + page.entity.name + "." + camelize(formItem.field.column.name) + "}]]"}) {
                        $('select[name=${camelize(formItem.field.column.name)}]').append(`<option value="${r"${it.id}"}" selected>${r"${it." + formItem.field.column.associate.formItemColumnName + "}"}</option>`)
                    } else {
                        $('select[name=${camelize(formItem.field.column.name)}]').append(`<option value="${r"${it.id}"}">${r"${it." + formItem.field.column.associate.formItemColumnName + "}"}</option>`)
                    }
                    </#if>
                })
                form.render('select')
            }
        })
        </#if>
        </#list>
    })
</script>

