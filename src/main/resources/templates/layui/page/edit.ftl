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
    <#if entity.table.associates??>
        <#function camelize(s)>
            <#return s
            ?replace('(^_+)|(_+$)', '', 'r')
            ?replace('\\_+(\\w)?', ' $1', 'r')
            ?replace('([A-Z])', ' $1', 'r')
            ?capitalize
            ?replace(' ' , '')
            ?uncap_first>
        </#function>
        <#list entity.table.associates as associate>
            <div class="layui-form-item">
                <label class="layui-form-label">${associate.formItemDescription}</label>
                <div class="layui-input-block">
                    <select name="${camelize(associate.sourceColumnName)}">
                        <option value="">请选择${associate.formItemDescription}</option>
                    </select>
                </div>
            </div>
        </#list>
    </#if>
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
                           placeholder="请输入${comment}" class="layui-input"/>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "PasswordFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <input type="password" name="${formItemName}"
                           <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                           placeholder="请输入${comment}" value="" class="layui-input"/>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "DateTimeFormItem" || formItem.class.simpleName == "DateFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <input type="text" name="${formItemName}" id="${formItemName}"
                           <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                           placeholder="请输入${comment}" value="" class="layui-input"/>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "SelectFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <select name="${formItemName}"
                            <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>>
                        <option value="">请选择${comment}</option>
                        <#list formItem.options as option>
                            <option value="${option.value}"
                                    th:selected="${r"${" + name + "." + formItemName + " == '" + option.value + "'}"}">${option.title}</option>
                        </#list>
                    </select>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "RadioFormItem">
            <div class="layui-form-item">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <#list formItem.options as option>
                        <input type="radio" name="${formItemName}" value="${option.value}" title="${option.title}"
                               <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                               th:checked="${r"${" + name + "." + formItemName + " == '" + option.value + "'}"}"/>
                    </#list>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "TextAreaFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label <#if formItem.require>required</#if>">${comment}</label>
                <div class="layui-input-block">
                    <textarea placeholder="请输入内容" class="layui-textarea" name="${formItemName}"
                              <#if formItem.require>lay-verify="required" lay-reqtext="${comment}不能为空"</#if>
                              th:text="${r"${" + name + "." + formItemName + "}"}">
                    </textarea>
                </div>
            </div>
        <#elseif formItem.class.simpleName == "FileFormItem">
            <div class="layui-form-item layui-form-text">
                <label class="layui-form-label">${comment}</label>
                <div class="layui-upload">
                    <button type="button" class="layui-btn layui-btn-normal" id="upload">选择文件</button>
                    <div class="layui-upload-list">
                        <table class="layui-table">
                            <thead>
                            <tr>
                                <th>文件名</th>
                                <th>大小</th>
                                <th>状态</th>
                                <th>操作</th>
                            </tr>
                            </thead>
                            <tbody id="file-list"></tbody>
                        </table>
                    </div>
                    <button type="button" class="layui-btn" id="upload-btn">开始上传</button>
                </div>
                <input name="${formItemName}" type="text" hidden>
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
    layui.use(['form', 'laydate', 'upload'], function () {
        let form = layui.form,
            $ = layui.$,
            laydate = layui.laydate,
            upload = layui.upload
        <#if entity.table.associates??>
        <#function camelize(s)>
        <#return s
            ?replace('(^_+)|(_+$)', '', 'r')
            ?replace('\\_+(\\w)?', ' $1', 'r')
            ?replace('([A-Z])', ' $1', 'r')
            ?capitalize
            ?replace(' ' , '')
            ?uncap_first>
        </#function>
        <#list entity.table.associates as associate>
        $.ajax({
            type: 'post',
            url: '/${camelize(associate.targetTableName)}/list',
            data: JSON.stringify({}),
            contentType: 'application/json',
            success: function (data) {
                data.data.forEach(it => {
                    if (it.id === ${r"[[${" + entity.name + "." + camelize(associate.sourceColumnName) + "}]]"}) {
                        $('select[name=${camelize(associate.sourceColumnName)}]').append(`<option value="${r"${it.id}"}" selected>${r"${it." + associate.formItemColumnName + "}"}</option>`)
                    } else {
                        $('select[name=${camelize(associate.sourceColumnName)}]').append(`<option value="${r"${it.id}"}">${r"${it." + associate.formItemColumnName + "}"}</option>`)
                    }
                })
                form.render('select')
            }
        })
        </#list>
        </#if>
        <#list form.formItems as formItem>
        <#if formItem.class.simpleName == "DateTimeFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'datetime',
            value: ${r"[[${#temporals.format(" + entity.name + "." + formItem.field.name + ", 'yyyy-MM-dd HH:mm:ss')}]]"}
        })
        <#elseif formItem.class.simpleName == "DateFormItem">
        laydate.render({
            elem: '#${formItem.field.name}',
            type: 'date',
            value: ${r"[[${#temporals.format(" + entity.name + "." + formItem.field.name + ", 'yyyy-MM-dd')}]]"}
        })
        <#elseif formItem.class.simpleName == "FileFormItem">
        //多文件列表示例
        let imageList = $('#file-list')
        let uploadListIns = upload.render({
            elem: '#upload',
            url: '/upload',
            accept: 'file',
            multiple: false,
            auto: false,
            bindAction: '#upload-btn',
            choose: function (obj) {
                let files = this.files = obj.pushFile() //将每次选择的文件追加到文件队列
                //读取本地文件
                obj.preview(function (index, file) {
                    let tr = $(`
                        <tr id="upload-${r"${index}"}">
                            <td>${r"${file.name}"}</td>
                            <td>${r"${(file.size / 1024).toFixed(1)}kb"}</td>
                            <td>等待上传</td>
                            <td>
                                <button class="layui-btn layui-btn-xs reload-btn layui-hide">重传</button>
                                <button class="layui-btn layui-btn-xs layui-btn-danger delete-btn">删除</button>
                            </td>
                        </tr>
                    `)
                    //单个重传
                    tr.find('.reload-btn').on('click', function () {
                        obj.upload(index, file)
                    })

                    //删除
                    tr.find('.delete-btn').on('click', function () {
                        delete files[index] //删除对应的文件
                        tr.remove()
                        uploadListIns.config.elem.next()[0].value = '' //清空 input file 值，以免删除后出现同名文件不可选
                    })
                    imageList.append(tr)
                })
            },
            done: function (res, index) {
                let tr = imageList.find(${r"`tr#upload-${index}`"}),
                    tds = tr.children()
                tds.eq(2).html('<span style="color: #5FB878;">上传成功</span>')
                tds.eq(3).html('') //清空操作
                $('input[name=${formItem.field.name}]').val(res.data)
                return delete this.files[index] //删除文件队列已经上传成功的文件
            },
            error: function () {
                let tr = imageList.find(${r"`tr#upload-${index}`"}),
                    tds = tr.children()
                tds.eq(2).html('<span style="color: #FF5722;">上传失败</span>')
                tds.eq(3).find('.reload-btn').removeClass('layui-hide') //显示重传
            }
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