<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<html lang="zh-cn">
<head>
    <link rel="stylesheet" type="text/css" href="./css/bootstrap.css">
    <link rel="stylesheet" type="text/css" href="./css/index.css">
    <meta charset="UTF-8" name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>垃圾分类查询</title>
</head>
<body class="bg-light">
<div class="container ">
    <%--上面的欢迎栏--%>
    <div class="row ">
        <div class="col-1 bg-">

        </div>
        <div class="col-10 " id="title">
            <div class="btn-group float-right btn-lg" role="group">
                <button id="loginModalButton" class="btn btn-success  " data-toggle="modal"
                        data-target="#loginModal">
                    登录
                </button>
                <button id="signupModalButton" class="btn btn-success  " data-toggle="modal"
                        data-target="#signupModal">
                    注册
                </button>
                <button id="signoutButton" class="btn btn-success  d-none" onclick="signout()">
                    退出
                </button>
            </div>
            <h1 class="text-center ">垃圾信息查询</h1>
        </div>
        <div class="col-1 bg-">

        </div>
    </div>
    <div class="row ">
        <div class="col-1 bg-">

        </div>
        <div class="col-10 bg-white">
            <div class="input-group my-2  ml-5 col-6">
                <input type="text" class="form-control" placeholder="输入垃圾名称" id="queryGarbageId">
                <div class="input-group-append">
                    <button type="button " class=" btn btn-success" id="" onclick="queryGarbageClick()">
                        查询
                    </button>
                    <button type="button" class="btn btn-success" onclick="manageGarbageClick()">我要管理</button>
                    <button id="manageGarbageButton" class="d-none" type="button " data-toggle="modal"
                            data-target="#manageGarbageModal">
                    </button>
                    <button id="manageUserButtonDisplay" type="button" class="btn btn-success d-none "
                            onclick="manageUserClick()">用户管理
                    </button>
                    <button id="manageUserButton" class="d-none" type="button " data-toggle="modal"
                            data-target="#manageUserModal">
                    </button>
                </div>
            </div>
            <table class="table table-sm  table-striped ">
                <thead>
                <tr>
                    <th scope="col" class="">id</th>
                    <th scope="col" class="">垃圾名称</th>
                    <th scope="col" class="">垃圾类别</th>
                    <th scope="col" class="">描述信息</th>
                </tr>
                </thead>
                <tbody id="garbaegInfoTable" class="">
                </tbody>
            </table>
            <div class="btn-group float-right" role="group">
                <button class="btn btn-success" onclick="previousPage()">上一页</button>
                <button class="btn btn-success" onclick="nextPage()">下一页</button>
            </div>
        </div>
        <div class="col-1 bg-">

        </div>
    </div>
</div>

<div class="modal fade" id="signupModal" tabindex="-1" role="dialog"
     data-backdrop="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!--注册框头部-->
            <div class="modal-header">
                <h4 class="modal-title">
                    欢迎注册！
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="closeSignup">
                    ×
                </button>
            </div>
            <!--注册框中间部分(from表单)-->
            <div class="modal-body">
                <form method="post" class="form-horizontal" role="form"
                <%--                      action="${pageContext.request.contextPath}/user/ServletSignup"--%>
                >
                    <!--用户框-->
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label" style="max-width: 30%;">用户名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="username" name="userename"
                                   placeholder="username" required="required">
                            <lable id="msg"></lable>
                        </div>
                    </div>
                    <!--密码框-->
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="password" name="password"
                                   placeholder="password" required="required">
                        </div>
                    </div>
                    <input type='hidden' name='username' id='usernameBase64' value='1'/>
                    <input type='hidden' name='password' id='passwordBase64' value='1'/>
                    <!--注册按钮-->
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" onclick="checkForm()" class="btn btn-primary" id="modalSignup">注册
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="loginModal" tabindex="-1" role="dialog"
     data-backdrop="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!--注册框头部-->
            <div class="modal-header">
                <h4 class="modal-title">
                    欢迎登录！
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="closeLogin">
                    ×
                </button>
            </div>
            <!--注册框中间部分(from表单)-->
            <div class="modal-body">
                <form method="post" class="form-horizontal" role="form">
                    <!--用户框-->
                    <div class="form-group">
                        <label for="username" class="col-sm-2 control-label" style="max-width: 30%;">用户名</label>
                        <div class="col-sm-10">
                            <input type="text" class="form-control" id="usernameLogin" name="userename"
                                   placeholder="username" required="required">
                            <lable id="msg"></lable>
                        </div>
                    </div>
                    <!--密码框-->
                    <div class="form-group">
                        <label for="password" class="col-sm-2 control-label">密码</label>
                        <div class="col-sm-10">
                            <input type="password" class="form-control" id="passwordLogin" name="password"
                                   placeholder="password" required="required">
                        </div>
                    </div>
                    <input type='hidden' name='username' id='usernameBase64Login' value='1'/>
                    <input type='hidden' name='password' id='passwordBase64Login' value='1'/>
                    <!--注册按钮-->
                    <div class="form-group">
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="button" onclick="checkFormLogin()" class="btn btn-primary" id="modalLogin">
                                登录
                            </button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="modal fade" id="manageGarbageModal" tabindex="-1" role="dialog"
     data-backdrop="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <!--注册框头部-->
            <div class="modal-header">
                <h4 class="modal-title">
                    信息管理
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true"
                        id="closeManageGarbageModal">
                    ×
                </button>
            </div>
            <div class="modal-body">
                <form onsubmit="return insertGarbage()">
                    <div class="row">
                        <div class="col">
                            <input id="garbageInsertGarbage" type="text" class="form-control" required="required"
                                   placeholder="垃圾名称">
                        </div>
                        <div class="col">
                            <input id="typeInsertGarbage" type="text" class="form-control" required="required"
                                   placeholder="垃圾类型">
                        </div>
                        <div class="col">
                            <input id="describeInsertGarbage" type="text" class="form-control" required="required"
                                   placeholder="垃圾描述（可不填）">
                        </div>
                        <div class="col">
                            <button id="insertGarbageButton" type="submit" class="btn btn-primary ">新增</button>
                        </div>
                    </div>
                </form>
                <form onsubmit="return updateGarbage()">
                    <div class="row">
                        <div class="col">
                            <input id="garbageUpdateGarbage" type="text" class="form-control" required="required"
                                   placeholder="垃圾名称">
                        </div>
                        <div class="col">
                            <input id="typeUpdateGarbage" type="text" class="form-control" required="required"
                                   placeholder="垃圾类型">
                        </div>
                        <div class="col">
                            <input id="describeUpdateGarbage" type="text" class="form-control" placeholder="垃圾描述（可不填）">
                        </div>
                        <div class="col">
                            <button id="updateGarbageButton" type="submit" class="btn btn-primary ">修改</button>
                        </div>
                    </div>
                </form>
                <form onsubmit="return deleteGarbage()">
                    <div class="row">
                        <div class="col">
                            <input id="garbageDeleteGarbage" type="text" class="form-control" required="required"
                                   placeholder="垃圾名称">
                        </div>
                        <div class="col">
                            <button id="deleteGarbageButton" type="submit" class="btn btn-primary ">删除</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>


    </div>
</div>
<div class="modal fade" id="manageUserModal" tabindex="-1" role="dialog"
     data-backdrop="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">
                    用户管理
                </h4>
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true" id="closeManageUserModal">
                    ×
                </button>
            </div>
            <div class="modal-body">
                <form onsubmit="return updateUser()">
                    <div class="row">
                        <div class="col">
                            <input id="usernameUpdateUser" type="text" class="form-control" required="required"
                                   placeholder="用户名">
                        </div>
                        <div class="col">
                            <%--                            <input id="isAdminUpdateUser" type="text" class="form-control" required="required"--%>
                            <%--                                   placeholder="管理员（0,1）">--%>
                            <select class="custom-select my-1 mr-sm-2" id="isAdminUpdateUser" title="暂不开放此功能">
                                <option selected value="0">0</option>
                                <option value="1">1</option>
                            </select>
                        </div>
                        <div class="col">
                            <input id="userRemarksUpdateUser" type="text" class="form-control" placeholder="用户描述（可不填）">
                        </div>
                        <div class="col">
                            <button id="updateUserButton" type="submit" class="btn btn-primary ">修改</button>
                        </div>
                    </div>
                </form>
                <form onsubmit="return deleteUser()">
                    <div class="row">
                        <div class="col">
                            <input id="usernameDeleteUser" type="text" class="form-control" required="required"
                                   placeholder="用户名">
                        </div>
                        <div class="col">
                            <button id="deleteUserButton" type="submit" class="btn btn-primary ">删除</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="toast" data-delay="2000" role="alert" id="toastAlert"
     style="position: fixed; right: 450px;top: 5px ">
    <div class="toast-body"></div>
</div>
<div class="toast" data-delay="5000" role="alert" id="toastQueryGarbage"
     style="position: fixed; right: 750px;top: 150px ">
    <button type="button " class="ml-2 mb-1 close float-right" data-dismiss="toast">
        <span>&times;</span>
    </button>
    <h5 class="mr-auto text-center" id="garbage"></h5>
    <div class="toast-body">
        <span id="type"></span><br/>
        <i id="describe"></i>
    </div>
</div>
<%--用于js获取上下文路径--%>
<input id="pageContext" type="hidden" value="${pageContext.request.contextPath}"/>
<div id="test">
    <lable>method</lable>
    <input type="text" id="method"><br/>
    <lable>path</lable>
    <input type="text" id="path"><br/>
    <lable>sendNum</lable>
    <input type="text" id="sendNum"><br/>
    <button onclick="sendAJAXMany()">sendAJAXMany</button>
</div>
</body>
<script>const pageContext = document.getElementById("pageContext").value;
</script>
<script src="js/test/test.js"></script>
<script src="js/jquery.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/bootstrap.bundle.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/tools.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/getDataRow.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/indexTable.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/signupAndLogin.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/index.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/checkUsernameRepeat.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/queryGarbage.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/manageGarbage.js" type="text/javascript" charset="UTF-8"></script>
<script src="js/manageUser.js" type="text/javascript" charset="UTF-8"></script>
<script>

</script>
</html>
