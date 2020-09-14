/**
 *@author  le
 *date:    2020/5/29
 *describe：
 */

function queryGarbageClick() {
    const queryGarbageId = document.getElementById("queryGarbageId");
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            const garbageQueryGarbage = document.getElementById("garbage");
            const typeQueryGarbage = document.getElementById("type");
            const describeQueryGarbage = document.getElementById("describe");
            console.log(xhr.responseText + "---responseText")
            //查询不到结果
            if (xhr.responseText == "false") {
                describeQueryGarbage.innerHTML = "No results.";
            } else {
                const garbageQueryInfo = JSON.parse(xhr.responseText)[0];
                console.log(garbageQueryGarbage.value);
                garbageQueryGarbage.innerHTML = garbageQueryInfo.garbage;
                typeQueryGarbage.innerHTML = garbageQueryInfo.type;
                describeQueryGarbage.innerHTML = garbageQueryInfo.describe;
            }
            $('#toastQueryGarbage').toast('show')
        }
    }
    xhr.open("GET", pageContext + "/garbage/ServletQueryGarbage?garbage=" +
        queryGarbageId.value, true);
    xhr.send();
}