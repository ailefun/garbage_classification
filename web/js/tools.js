/**
 *@author  le
 *date:    2020/5/30
 *describe：
 */
//手撸吐司
function smallToast(message, time) {
    const toastAlert = $("#toastAlert")
    toastAlert.children("div").text(message);
    toastAlert.attr("data-delay", time)
    toastAlert.toast('show');
}
// smallToast("12233333333", 1000)



//手撸阿贾克斯
// let method = "POST";
// let url = pageContext + "/garbage/ServletInsertGarbage?garbage=" +
//     garbageInsertGarbage.value +
//     "&type=" + typeInsertGarbage.value +
//     "&describe=" + describeInsertGarbage.value;
//
// function funTrue() {
//     alert("successed.")
// }
// function funFalse() {
//     alert("failed.")
// }
//
// function ajax(method, url, funTrue, funFalse) {
//     const xhr = new XMLHttpRequest();
//     // 异步写成死的
//     xhr.open(method, url, true);
//     xhr.onreadystatechange = function () {
//         if (xhr.readyState == 4 && xhr.status == 200) {
//             console.log(xhr.responseText + "---responseText")
//             if (xhr.responseText == "true") {
//                 funTrue();
//             } else {
//                 funFalse();
//             }
//         }
//     }
//     xhr.send();
// }