<#if projectType == "ssm">
<%@ page contentType="text/html; charset=utf-8"%>
<%@ page isELIgnored="true" %>
</#if>
<!DOCTYPE html>
<html xmlns:th="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="UTF-8">
    <title>后台管理-登录</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="format-detection" content="telephone=no">
    <#if projectType == "ssm">
        <link rel="stylesheet" href="<%=request.getContextPath()%>/static/lib/layui/css/layui.css" media="all">
    <#else>
        <link rel="stylesheet" th:href="@{/lib/layui/css/layui.css}" media="all">
    </#if>
    <!--[if lt IE 9]>
    <script src="https://cdn.staticfile.org/html5shiv/r29/html5.min.js"></script>
    <script src="https://cdn.staticfile.org/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        body {background-image:url("../images/bg.jpg");height:100%;width:100%;}
        #container{height:100%;width:100%;}
        input:-webkit-autofill {-webkit-box-shadow:inset 0 0 0 1000px #fff;background-color:transparent;}
        .admin-login-background {width:300px;height:300px;position:absolute;left:50%;top:40%;margin-left:-150px;margin-top:-100px;}
        .admin-header {text-align:center;margin-bottom:20px;color:#ffffff;font-weight:bold;font-size:40px}
        .admin-input {border-top-style:none;border-right-style:solid;border-bottom-style:solid;border-left-style:solid;height:50px;width:300px;padding-bottom:0px;}
        .admin-input::-webkit-input-placeholder {color:#a78369}
        .layui-icon-username {color:#a78369 !important;}
        .layui-icon-username:hover {color:#9dadce !important;}
        .layui-icon-password {color:#a78369 !important;}
        .layui-icon-password:hover {color:#9dadce !important;}
        .admin-input-username {border-top-style:solid;border-radius:10px 10px 0 0;}
        .admin-button {margin-top:20px;font-weight:bold;font-size:18px;width:300px;height:50px;border-radius:5px;background-color:#a78369;border:1px solid #d8b29f}
        .admin-icon {margin-left:260px;margin-top:10px;font-size:30px;}
        i {position:absolute;}
    </style>
</head>
<body th:style="'background:url(' + @{/images/bg.jpg} + ');'">
<div id="container">
    <div class="admin-login-background">
        <div class="admin-header">
            <span>登录页面</span>
        </div>
        <div class="layui-form login-form">
            <form class="layui-form" action="">
                <div>
                    <i class="layui-icon layui-icon-username admin-icon"></i>
                    <input type="text" name="username" placeholder="请输入用户名" autocomplete="off" class="layui-input admin-input admin-input-username" value="">
                </div>
                <div>
                    <i class="layui-icon layui-icon-password admin-icon"></i>
                    <input type="password" name="password" placeholder="请输入密码" autocomplete="off" class="layui-input admin-input" value="">
                </div>
                <div class="layui-form-item">
                    <select name="role" lay-filter="role" lay-verify="required" lay-reqtext="请选择角色">
                        <option value="">请选择角色</option>
                    </select>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn admin-button" lay-submit="" lay-filter="login">登 录</button>
                </div>
            </form>
            <button class="layui-btn admin-button register-btn">注 册</button>
        </div>
    </div>
</div>
</body>
</html>
<#if projectType == "ssm">
    <script src="<%=request.getContextPath() %>/static/lib/layui/layui.js" charset="utf-8"></script>
    <script src="<%=request.getContextPath() %>/static/lib/jquery-3.4.1/jquery-3.4.1.min.js" charset="utf-8"></script>
    <script src="<%=request.getContextPath() %>/static/lib/jq-module/jquery.particleground.min.js" charset="utf-8"></script>
<#else>
    <script th:src="@{/lib/jquery-3.4.1/jquery-3.4.1.min.js}" charset="utf-8"></script>
    <script th:src="@{/lib/layui/layui.js}" charset="utf-8"></script>
    <script th:src="@{/lib/jq-module/jquery.particleground.min.js}" charset="utf-8"></script>
</#if>
<script>
    layui.use(['form'], function () {
        var form = layui.form,
            $ = layui.jquery;

        // 粒子线条背景
        $(document).ready(function(){
            $('.layui-container').particleground({
                dotColor:'#7ec7fd',
                lineColor:'#7ec7fd'
            });
        });
        $.ajax({
            url: '/role/list',
            type: 'get',
            success: function (data) {
                data.data.forEach(role => {
                    $('select[name=role]').append(${r'`<option value="${role.name}">${role.description}</option>`'})
                });
                form.render('select');
            }
        });

        // 进行登录操作
        form.on('submit(login)', function (data) {
            $.ajax({
                url: '/login',
                type: 'post',
                data: JSON.stringify(data.field),
                contentType: 'application/json',
                success: function () {
                    window.location.href = ${r"`http://localhost:${window.location.port}`"}
                },
                error: function (data) {
                    layer.msg('用户名或密码错误')
                }
            });
            return false;
        });

        $('.register-btn').click(function () {
            window.location.href = ${r"`http://${window.location.host}/registerPage`"}
        })
    });
</script>