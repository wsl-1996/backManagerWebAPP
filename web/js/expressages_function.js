var dt = document.getElementById("dt");

var data;

var hanghao=1;

function init(){

    data=document.getElementById("expressagesList");

    //deletearow();

    // tianjia(2,"zhangsan","路人甲");

}

//添加一行

function tianjia(expressageid,expressageName,expresage_shipAddress){

    var rows=data.rows;

    for(var i=0;i<rows.length;i++){

        if(expressageid==rows[i].cells[0].innerText){

            window.alert("已经存在该ID的用户，请重新输入");

            return;

        }

    }

    var atr=data.insertRow(rows.length);

    var tda=atr.insertCell();

    var tdb=atr.insertCell();

    var tdc=atr.insertCell();

    var tdd=atr.insertCell();

    atr.id="tr"+hanghao;

    tda.innerText=expressageid;                 //输出的是快递的ID

    tdb.innerText=expressageName;

    tdc.innerText=expresage_shipAddress;

    tdd.innerHTML="<button class='btn btn-primary'  onclick=\"xiugai('"+atr.id+"')\">修改</button><button class='btn btn-primary'  onclick=\"deletearow('"+atr.id+"')\">删除</button>";

    hanghao++;

}

function xiugai(atrid){

    var expressageid=document.getElementById("expressageid");

    var expressageName=document.getElementById("expressageName");

    var expresage_shipAddress=document.getElementById("expresage_shipAddress");

    var rows=data.rows;

    for(var i=0;i<rows.length;i++){

        var atr=rows[i];

        if(atr.id==atrid){

            // cols = atr.getElementByTagName('td');

//alert(cols.length);

            var a = atr.cells[0].innerText;

            var b = atr.cells[1].innerText;

            var c = atr.cells[2].innerText;

            document.getElementById("expressageid").value=a;

            document.getElementById("expressageName").value=b;

            document.getElementById("expresage_shipAddress").value=c;

        }

    }

}

function deletearow(atrid){

    if(confirm("确定要删除吗？")){

        var rows=data.rows;

        for(var i=0;i<rows.length;i++){

            var atr=rows[i];

            if(atr.id==atrid){

                data.deleteRow(i);

            }

        }

    }

}

function dotianjia(){

    var expressageid=document.getElementById("expressageid");

    var expressageName=document.getElementById("expressageName");

    var expresage_shipAddress=document.getElementById("expresage_shipAddress");

    tianjia(expressageid.value,expressageName.value,expresage_shipAddress.value);

}

function dobaocun(){

    var winname=window.open('','_blank');

    winname.document.open('text/html','replace');

    var code=document.getElementById("dt").innerHTML;

    winname.document.write(code);

    winname.document.execCommand('saveas','table.html');

    winname.close();

}