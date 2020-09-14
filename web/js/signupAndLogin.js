/**
 *@author  le
 *date:    2020/5/29
 *describe：
 */
// let sessionObject = undefined;//验证能否进入管理页面，这个对象下面三个函数都要用，在全局声明
const signupModalButton = $("#signupModalButton");
const loginModalButton = $("#loginModalButton");
const manageUserButtonDisplay = $("#manageUserButtonDisplay");
const signoutButton = $("#signoutButton");

//点击表内注册按钮后触发的事件
function checkForm() {
    const username = document.getElementById("username");
    const password = document.getElementById("password");
    const usernameBase64 = document.getElementById("usernameBase64");
    const passwordBase64 = document.getElementById("passwordBase64");

    // 对前端密码进行一个简单的编码,并发送请求

    usernameBase64.value = btoa(encodeURIComponent(username.value));
    passwordBase64.value = btoa(encodeURIComponent(password.value));
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            console.log(xhr.responseText + "---xhr.responseText")
            if (xhr.responseText == "true") {
                console.log(5)
                // $('#toastSignup').toast('show');
                smallToast("Signup succeed。", 2000)
                document.getElementById("closeSignup").click();
            } else {
                console.log(false)
            }
        }
    }
    console.log(username.value + "---username")
    console.log(usernameBase64.value + "---usernameBase64.value")
    xhr.open("POST", pageContext + "/user/ServletSignup?username=" +
        usernameBase64.value + "&password=" + passwordBase64.value, true);
    xhr.send();
}
//点击表内登录按钮后触发的事件
function checkFormLogin() {
    const usernameLogin = document.getElementById("usernameLogin");
    const passwordLogin = document.getElementById("passwordLogin");
    const usernameBase64Login = document.getElementById("usernameBase64Login");
    const passwordBase64Login = document.getElementById("passwordBase64Login");
    // 对前端密码进行一个简单的编码,并发送请求
    usernameBase64Login.value = btoa(encodeURIComponent(usernameLogin.value));
    passwordBase64Login.value = btoa(encodeURIComponent(passwordLogin.value));
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 && xhr.status == 200) {
            console.log(xhr.responseText + "---xhr.responseText == true")
            if (xhr.responseText == "true") {
                document.getElementById("closeLogin").click();
                // $('#toastLogin').toast('show')
                smallToast("Login succeed.", 2000)
                let xhrInner = new XMLHttpRequest();
                xhrInner.onreadystatechange = function () {
                    if (xhrInner.readyState == 4 && xhrInner.status == 200) {
                        console.log(xhrInner.responseText + "---/user/ServletGetSession");
                        window.sessionObject = JSON.parse(xhrInner.responseText);
                        signupModalButton.addClass("d-none");
                        loginModalButton.addClass("d-none");
                        signoutButton.removeClass("d-none");
                        if (window.sessionObject != undefined && window.sessionObject[0]["isAdmin"] == 1) {
                            manageUserButtonDisplay.removeClass("d-none");
                        }
                    }

                }
                xhrInner.open("POST", pageContext + "/user/ServletGetSession", true);
                xhrInner.send();
            } else {
                alert("Wrong user name or password.")
            }
        }
    }
    console.log(usernameBase64Login.value + "---usernameBase64Login.value")
    console.log(usernameBase64Login.value + "---usernameBase64Login.value")
    xhr.open("POST", pageContext + "/user/ServletLogin?username=" +
        usernameBase64Login.value + "&password=" + passwordBase64Login.value, true);
    xhr.send();

}

function signout() {
    var xhr = new XMLHttpRequest()
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4 || xhr.status == 200) {
            console.log("Exit succeed。");
            smallToast("Successful exit.", 2000)
        }
    }
    xhr.open("GET", pageContext + "/user/ServletInvalidateSession", true);
    xhr.send();
    window.sessionObject = undefined;
    signupModalButton.removeClass("d-none");
    loginModalButton.removeClass("d-none");
    signoutButton.addClass("d-none");
    manageUserButtonDisplay.addClass("d-none");
    if (window.sessionObject != undefined && window.sessionObject[0]["isAdmin"] == 1) {
        manageUserButtonDisplay.addClass("d-none");
    }

}
/* 使用户主动刷新页面后，按钮依据登录状态正常显示*/
function updatePage() {
    let xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText + "---/user/ServletGetSession");
            if (xhr.responseText != "") {
                // console.log("xhr.responseText != 空串 ")
                window.sessionObject = JSON.parse(xhr.responseText);
                console.log(window.sessionObject + "---window.sessionObject")
                console.log(window.sessionObject != undefined + "--window.sessionObject != undefined")
                if (window.sessionObject != undefined) {
                    console.log("running...")
                    signupModalButton.addClass("d-none");
                    loginModalButton.addClass("d-none");
                    signoutButton.removeClass("d-none");
                    if (window.sessionObject[0]["isAdmin"] == 1) {
                        manageUserButtonDisplay.removeClass("d-none");
                    }
                }
            }

        }
    }
    xhr.open("POST", pageContext + "/user/ServletGetSession", true);
    xhr.send();
}