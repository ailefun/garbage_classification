/**
 *@author  le
 *date:    2020/5/30
 *describe：
 */



function manageGarbageClick() {
    //先判断sessionObject，防止报错
    if (window.sessionObject == undefined || window.sessionObject[0].username == null) {
        alert("please login.");
    } else {
        const manageGarbageButton = document.getElementById("manageGarbageButton");
        manageGarbageButton.click();
    }
}
function insertGarbage() {
    const garbageInsertGarbage = document.getElementById("garbageInsertGarbage");
    const typeInsertGarbage = document.getElementById("typeInsertGarbage");
    const describeInsertGarbage = document.getElementById("describeInsertGarbage");
    const xhr = new XMLHttpRequest();
    xhr.open("POST", pageContext + "/garbage/ServletInsertGarbage?garbage=" +
        garbageInsertGarbage.value +
        "&type=" + typeInsertGarbage.value +
        "&describe=" + describeInsertGarbage.value, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText + "---responseText")
            if (xhr.responseText == "true"){
                alert("successed.")
            }else{
                alert("failed.")
            }
        }
    }
    xhr.send();
    return false;
}
function updateGarbage() {
    const garbageUpdateGarbage = document.getElementById("garbageUpdateGarbage");
    const typeUpdateGarbage = document.getElementById("typeUpdateGarbage");
    const describeUpdateGarbage = document.getElementById("describeUpdateGarbage");
    const xhr = new XMLHttpRequest();
    xhr.open("POST", pageContext + "/garbage/ServletUpdateGarbage?garbage=" +
        garbageUpdateGarbage.value +
        "&type=" + typeUpdateGarbage.value +
        "&describe=" + describeUpdateGarbage.value, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText + "---responseText")
            if (xhr.responseText == "true"){
                alert("successed.")
            }else{
                alert("failed.")
            }
        }
    }
    xhr.send();
    return false;
}

function deleteGarbage() {
    const garbageDeleteGarbage = document.getElementById("garbageDeleteGarbage");
    const xhr = new XMLHttpRequest();
    xhr.open("POST", pageContext + "/garbage/ServletDeleteGarbage?garbage=" +
        garbageDeleteGarbage.value, true);
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(xhr.responseText + "---responseText")
            if (xhr.responseText == "true"){
                alert("successed.")
            }else{
                alert("failed.")
            }
        }
    }
    xhr.send();
    return false;
}