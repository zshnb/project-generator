<#if projectType == "ssm">
<%@ page contentType="text/html; charset=utf-8"%>
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
        html, body {width: 100%;height: 100%;overflow: hidden}
        body {background: #1E9FFF;}
        body:after {content:'';background-repeat:no-repeat;background-size:cover;-webkit-filter:blur(3px);-moz-filter:blur(3px);-o-filter:blur(3px);-ms-filter:blur(3px);filter:blur(3px);position:absolute;top:0;left:0;right:0;bottom:0;z-index:-1;}
        .layui-container {width: 100%;height: 100%;overflow: hidden}
        .admin-login-background {width:360px;height:300px;position:absolute;left:50%;top:40%;margin-left:-180px;margin-top:-100px;}
        .logo-title {text-align:center;letter-spacing:2px;padding:14px 0;}
        .logo-title h1 {color:#1E9FFF;font-size:25px;font-weight:bold;}
        .login-form {background-color:#fff;border:1px solid #fff;border-radius:3px;padding:14px 20px;box-shadow:0 0 8px #eeeeee;}
        .login-form .layui-form-item {position:relative;}
        .login-form .layui-form-item label {position:absolute;left:1px;top:1px;width:38px;line-height:36px;text-align:center;color:#d2d2d2;}
        .login-form .layui-form-item input {padding-left:36px;}
        .captcha-img img { border:1px solid #e6e6e6;height:36px;width:100%;}
    </style>
</head>
<body th:style="'background:url(' + @{/images/background.jpg} + ');'">
<div class="layui-container">
    <div class="admin-login-background">
        <div class="layui-form login-form">
            <form class="layui-form" action="">
                <div class="layui-form-item logo-title">
                    <h1>登录页面</h1>
                </div>
                <div class="layui-form-item">
                    <input type="text" name="username" lay-verify="required|account"
                           placeholder="用户名" autocomplete="off" class="layui-input" lay-reqtext="用户名不能为空">
                </div>
                <div class="layui-form-item">
                    <input type="password" name="password" lay-verify="required|password"
                           placeholder="密码" autocomplete="off" class="layui-input" lay-reqtext="密码不能为空">
                </div>
                <div class="layui-form-item">
                    <select name="role" lay-filter="role" lay-verify="required" lay-reqtext="请选择角色">
                        <option value="">请选择角色</option>
                    </select>
                </div>
                <div class="layui-form-item">
                    <button class="layui-btn layui-btn layui-btn-normal layui-btn-fluid" lay-submit="" lay-filter="login">登 录</button>
                </div>
            </form>
            <button class="layui-btn layui-btn layui-btn-normal layui-btn-fluid register-btn">注 册</button>
        </div>
    </div>
</div>
<#if projectType == "ssm">
    <script src="<%=request.getContextPath() %>/static/lib/layui/layui.js" charset="utf-8"></script>>
    <script src="<%=request.getContextPath() %>/static/lib/jquery-3.4.1/jquery-3.4.1.min.js" charset="utf-8"></script>>
    <script src="<%=request.getContextPath() %>/static/lib/jq-module/jquery.particleground.min.js" charset="utf-8"></script>>
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
                    window.location.href = 'http://localhost:8081'
                },
                error: function (data) {
                    layer.msg('用户名或密码错误')
                }
            });
            return false;
        });

        $('.register-btn').click(function () {
            window.location.href = 'http://localhost:8081/registerPage'
        })
    });
</script>
</body>
</html>