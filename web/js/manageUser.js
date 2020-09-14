/**
 *@author  le
 *date:    2020/5/30
 *describe：
 */
function manageUserClick() {
    const manageUserButton = document.getElementById("manageUserButton");
    if (window.sessionObject == undefined || sessionObject[0]["isAdmin"] != 1) {
        alert("You're not the administrator.");
    } else {
        manageUserButton.click();
    }
}

function updateUser() {
    const usernameUpdateUser = document.getElementById("usernameUpdateUser");
    const isAdminUpdateUser = document.getElementById("isAdminUpdateUser");
    const userRemarksUpdateUser = document.getElementById("userRemarksUpdateUser");
    const xhr = new XMLHttpRequest();
    xhr.open("POST", pageContext + "/admin/ServletUpdateUser?username=" +
        usernameUpdateUser.value +
        "&isAdmin=" + isAdminUpdateUser.value +
        "&userRemarks=" + userRemarksUpdateUser.value, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText + "---responseText")
            if (xhr.responseText == "true") {
                alert("successed.")
            } else {
                alert("failed.")
            }
        }
    }
    xhr.send();
    return false;
}

function deleteUser() {
    const usernameDeleteUser = document.getElementById("usernameDeleteUser");
    const xhr = new XMLHttpRequest();
    xhr.open("POST", pageContext + "/admin/ServletDeleteUser?username=" +
        usernameDeleteUser.value, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText + "---responseText")
            if (xhr.responseText == "true") {
                alert("successed.")
            } else {
                alert("failed.")
            }
        }
    }
    xhr.send();
    return false;
}