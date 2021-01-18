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
    <link rel="stylesheet" th:href="@{/lib/jq-module/zyupload/zyupload-1.0.0.min.css}" media="all">
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
        <#if formItem.class.simpleName == "InputFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <input type="text" name="${formItemName}"
                            <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                           placeholder="请输入${comment}" value="" class="layui-input">
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
                            <option value="${option.value}">${option.title}</option>
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
                               <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>>
                    </#list>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "TextAreaFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" name="${formItemName}"
                              <#if formItem.require>lay-verify="required"
                              lay-reqtext="${comment}不能为空"</#if>></textarea>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "ImageFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-input-block">
                    <div id="${formItemName}" class="${formItemName}"></div>
                    <input name="${formItemName}" type="text" hidden>
                </div>
            </div>
        </#if>
    </#list>
    <div class="layui-form-item">
        <div class="layui-input-block">
            <button class="layui-btn" lay-submit="" lay-filter="save-btn">立即提交</button>
        </div>
    </div>
</div>
<script th:src="@{/lib/layui-v2.5.5/layui.js}" charset="utf-8"></script>
<script th:src="@{/lib/jquery-3.4.1/jquery-3.4.1.min.js}" charset="utf-8"></script>
<script th:src="@{/lib/jq-module/zyupload/zyupload-1.0.0.min.js}" charset="utf-8"></script>
<script>
    layui.use(['form', 'laydate'], function () {
        let form = layui.form,
            $ = layui.$,
            laydate = layui.laydate
        <#list form.formItems as formItem>
        <#if formItem.class.simpleName == "DateTimeFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'datetime'
        })
        <#elseif formItem.class.simpleName == "ImageFormItem">
        $('#${formItem.field.name}').zyUpload({
            width: '650px',                 // 宽度
            height: '200px',
            itemWidth: '140px',                 // 文件项的宽度
            itemHeight: '115px',                 // 文件项的高度
            url: '/upload',  // 上传文件的路径
            fileType: ['jpg', 'png'],// 上传文件的类型
            fileSize: 51200000,                // 上传文件的大小
            multiple: false,                    // 是否可以多个文件上传
            dragDrop: false,                    // 是否可以拖动上传文件
            tailor: false,                    // 是否可以裁剪图片
            del: true,                    // 是否可以删除文件
            finishDel: false,  				  // 是否在上传文件完成后删除预览
            /* 外部获得的回调接口 */
            onSelect: function (selectFiles, allFiles) {    // 选择文件的回调方法  selectFile:当前选中的文件  allFiles:还没上传的全部文件
            },
            onDelete: function (file, files) {              // 删除一个文件的回调方法 file:当前删除的文件  files:删除之后的文件
            },
            onSuccess: function (file, response) {          // 文件上传成功的回调方法
                $('input[name=${formItem.field.name}]').val(JSON.parse(response).data)
            },
            onFailure: function (file, response) {          // 文件上传失败的回调方法
            },
        })
        $('#rapidAddImg').remove()
        </#if>
        </#list>

        //监听提交
        form.on('submit(save-btn)', function (data) {
            $.ajax({
                url: '/${entity.name}/add',
                type: 'post',
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