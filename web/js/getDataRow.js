/**
 *@author  le
 *date:    2020/5/29
 *describe：
 */
let id = 0;
function getDataRow(h) {
    const row = document.createElement('tr'); //创建行
    const idCell = document.createElement('td'); //创建第一列id
    idCell.innerHTML = ++id; //填充数据
    row.appendChild(idCell); //加入行  ，下面类似
    const garbageCell = document.createElement('td');//创建第二列name
    garbageCell.innerHTML = h.garbage;
    row.appendChild(garbageCell);
    const typeCell = document.createElement('td');//创建第三列job
    typeCell.innerHTML = h.type;
    row.appendChild(typeCell);
    const describeCell = document.createElement('td');//创建第二列name
    describeCell.innerHTML = h.describe;
    row.appendChild(describeCell);
    return row; //返回tr数据
}
