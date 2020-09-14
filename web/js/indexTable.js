/**
 *@author  le
 *date:    2020/5/29
 *describe：分页需要的个函数 previousPage 和 nextPage
 */
let trList;//存储tr数组
let trListLenth;//信息的条数，不包括表头的tr标签
const PAGE_ROW = 15;//每页显示的行数
let pageNum = 0;//当前页码

function garbageQueryAll() {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState == 4) {
            console.log(1)
            console.log(2)
            let garbageInfo = JSON.parse(xhr.responseText.toLowerCase());
            console.log(3)
            const garbaegInfoTable = document.getElementById('garbaegInfoTable');//分页用到
            const garbageInfoLength = garbageInfo.length;
            for (let i = 0; i < garbageInfoLength; i++) {
                const trow = getDataRow(garbageInfo[i]);
                garbaegInfoTable.appendChild(trow);
            }
            console.log(4)
            //函数执行后才产生的元素，函数外要用，因此应在函数外声明，在函数内获取
            trList = document.getElementsByTagName("tr");
            trListLenth = trList.length - 1;
            nextPage();
        }
    }
    xhr.open("GET", pageContext + "/garbage/ServletQueryGarbageAll", true);
    xhr.send();
}

//上一页函数
function previousPage() {
    if (pageNum != 1) {//不是一说明现在不在第一页
        pageNum -= 2; //页数减二就是上上页，那么执行下一页的函数，就会跳到上一页
    } else {//如果翻到了第一页，就返回最后一页
        pageNum = Math.ceil(trListLenth / PAGE_ROW) - 1;//总页数减一是倒数第二页，那么执行下一页的函数，就会跳到最后一页
    }
    nextPage();
}
function nextPage() {
    if (pageNum == Math.ceil(trListLenth / PAGE_ROW)) {
        pageNum = 0;//如果翻到了最后一页，就返回第一页
    }
    //实现需求的主要代码，根据页码隐藏和显示数据
    for (let i = 1; i <= trListLenth; i++) {
        if (i <= (pageNum) * PAGE_ROW || i > (pageNum + 1) * PAGE_ROW) {
            trList[i].setAttribute("style", "display:none");//隐藏不应显示的数据
        } else {
            trList[i].setAttribute("style", "display:table row");//显示该显示的数据
        }
    }
    pageNum++;//当前页数加一
}