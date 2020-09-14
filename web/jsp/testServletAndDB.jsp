<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html>
<head>
    <title>useBean 实例</title>
</head>
<body>
<form id="signupForm"
<%--      action="${pageContext.request.contextPath}/user/ServletSignup"--%>
      action="${pageContext.request.contextPath}/user/ServletLogin"
      method="post">
    <lable>username:</lable>
    <input id="username" type="text">
    <lable>password:</lable>
    <input id="password" type="password">
    <input type='hidden' name='username' id='usernameBase64' value='1'/>
    <input type='hidden' name='password' id='passwordBase64' value='1'/>
    <%--    为什么form里面的button一点页面就会自动提交？--%>
    <%--    我们在利用button标签写一个按钮且没有指定其type属性时，浏览器会被默认指定为submit
             ，如果这个按钮不是做提交表单的，切记一定要设置其属性type="button"。--%>
    <button type='submit' onclick="checkForm()">登录</button>

</form>
<%--测试删除用户--%>
<form id="DeleteUser"
      action="${pageContext.request.contextPath}/admin/ServletDeleteUser"
      method="post">
    <lable>username:</lable>
    <input id="usernameDeleteUser" name="username" type="text">
    <button type='submit'>删除</button>
</form>
<%--测试queryUser用户--%>
<form id="QueryUser"
      action="${pageContext.request.contextPath}/admin/ServletQueryUser"
      method="post">
    <lable>username:</lable>
    <input id="usernameQueryUser" name="username" type="text">
    <button type='submit'>查询</button>
</form>
<%--测试update用户--%>
<form id="UpdateUser"
      action="${pageContext.request.contextPath}/admin/ServletUpdateUser"
      method="post">
    <lable>username:</lable>
    <input id="usernameUpdateUser" name="username" type="text">
    <lable>isAdmin:</lable>
    <input type="text" name="isAdmin">
    <lable>userRemarks:</lable>
    <input type="text" name="userRemarks">

    <button type='submit'>更新</button>
</form>
<p>测试Garbage--------------------------</p>
<%--测试QueryGarbage--%>
<form id="QueryGarbage"
      action="${pageContext.request.contextPath}/garbage/ServletQueryGarbage"
      method="post">
    <lable>garbage:</lable>
    <input id="garbageQueryGarbage" name="garbage" type="text">
    <button type='submit'>查询</button>
</form>
<%--测试InsertGarbage--%>
<form id="InsertGarbage"
      action="${pageContext.request.contextPath}/garbage/ServletInsertGarbage"
      method="post">
    <lable>garbage:</lable>
    <input id="garbageInsertGarbage" name="garbage" type="text">
    <lable>type:</lable>
    <input id="typeInsertGarbage" name="type" type="text">
    <lable>describe:</lable>
    <input id="describeInsertGarbage" name="describe" type="text">
    <lable>garbageRemarks:</lable>
    <input id="garbageRemarksInsertGarbage" name="garbageRemarks" type="text">
    <button type='submit'>插入</button>
</form>
<%--测试DeleteGarbage--%>
<form id="DeleteGarbage"
      action="${pageContext.request.contextPath}/garbage/ServletDeleteGarbage"
      method="post">
    <lable>garbage:</lable>
    <input id="garbageDeleteGarbage" name="garbage" type="text">
    <button type='submit'>删除</button>
</form>
<%--测试UpdateGarbage--%>
<form id="UpdateGarbage"
      action="${pageContext.request.contextPath}/garbage/ServletUpdateGarbage"
      method="post">
    <lable>garbage:</lable>
    <input id="garbageUpdateGarbage" name="garbage" type="text">
    <lable>type:</lable>
    <input id="typeUpdateGarbage" name="type" type="text">
    <lable>describe:</lable>
    <input id="describeUpdateGarbage" name="describe" type="text">
    <button type='submit'>更新</button>
</form>
<%--测试UpdateGarbage--%>
<form id="QueryGarbageAll"
      action="${pageContext.request.contextPath}/garbage/ServletQueryGarbageAll"
      method="post">
    <button type='submit'>QueryGarbageAll</button>
</form>
<script>
    /*函数声明提前，不能在函数里用getElementById*/
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const usernameBase64 = document.getElementById("usernameBase64");
    const passwordBase64 = document.getElementById("passwordBase64");

    function checkForm() {
        // 对前端密码进行一个简单的编码。
        usernameBase64.value = btoa(username.value);
        passwordBase64.value = btoa(password.value);
        console.log(username.value);
        console.log(password.value);
        console.log(usernameBase64.value);
        console.log(passwordBase64.value);
        return true;
    }
</script>


</body>
</html>

