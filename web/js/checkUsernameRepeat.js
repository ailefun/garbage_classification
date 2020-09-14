/**
 *@author  le
 *date:    2020/5/29
 *describe：检查用户名是否重复，在用户输入字符的时候发送请求
 */
const nameElenment = document.getElementById("username");
nameElenment.oninput = function () {
    const username = this.value;
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            if (xhr.status == 200) {
                const msg = document.getElementById("msg");
                console.log(xhr.responseText + "responseText")
                if (xhr.responseText == "true") {//说明该用户名，已经存在
                    msg.innerHTML = "<span class='text-danger'>\n" +
                        "User name already exists</span>"
                    document.getElementById("modalSignup").setAttribute("disabled", "disabled");
                } else {
                    msg.innerHTML = "<span class='text-success'>\n" +
                        "Welcome to sign up</span>"
                    document.getElementById("modalSignup").removeAttribute("disabled");
                }
            }
        }
    }
    xhr.open("GET", pageContext + "/user/CheckUsername?username=" + username);
    xhr.send();
}

