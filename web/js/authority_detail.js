$(function(){
    var d1 = $.Deferred();
    var d2 = $.Deferred();
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    var authorityid = getQueryString("authorityid");
    var author;

    //获取管理员权限信息
    function getAdminInfo(authorityid) {
        $.ajax({
            url:"/ketuan/authoritys/getAuthorById?authorityid="+authorityid,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("获取管理员信息失败！"+data["failedMessage"]);
                }
                else{
                    var authority = data["data"]["authority"];
                    $("#author_name")[0].innerHTML = authority.adminName;
                    $("#adminCount")[0].innerHTML = authority.adminCount;
                    $("#authorityid")[0].innerHTML = authority.authorityId;
                    author = authority.adminAuthor;
                    d1.resolve("admininfo");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取管理员信息失败！"+JSON.stringify(e));
            }
        })
        
    }
    getAdminInfo(authorityid);
    //获取所有权限
    var authoritys;
    function getAllAuthorDefine() {
        $.ajax({
            url:"/ketuan/backmanage/getallauthordefine",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("获取权限定义信息失败！"+data["failedMessage"]);
                }
                else{
                    authoritys = data["data"]["authorityDefines"];
                    d2.resolve("authorityDefine");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取权限定义信息失败！"+JSON.stringify(e));
            }
        })
    }
    getAllAuthorDefine();

    function deleteAuth() {
        var btnId = $(this).attr("id");

        console.info("删除权限:"+btnId);
        $.ajax({
            url:"/ketuan/backmanage/deleteAuth?btnId="+btnId+"&auid="+authorityid,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("删除管理权限失败！"+data["failedMessage"]);
                }
                else{
                    console.info("管理员："+authorityid+"删除权限"+btnId+"成功。");
                    alert("管理员："+authorityid+"删除权限"+btnId+"成功");
                    $("#has_"+btnId)[0].innerHTML="";
                    $("#"+btnId)[0].innerHTML = "添加权限";
                    $("#"+btnId).unbind();
                    console.info("移除btn的所有绑定事件");
                    $("#"+btnId).on("click", addAuth);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("删除管理权限失败！"+JSON.stringify(e));
            }
        })
    }

    function addAuth() {
        var btnId = $(this).attr("id");
        console.info("添加权限:"+btnId);
        $.ajax({
            url:"/ketuan/backmanage/addAuth?btnId="+btnId+"&auid="+authorityid,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("添加管理权限失败！"+data["failedMessage"]);
                }
                else{
                    console.info("管理员："+authorityid+"添加权限"+btnId+"成功。");
                    alert("管理员："+authorityid+"添加权限"+btnId+"成功");
                    $("#has_"+btnId)[0].innerHTML='<span class="glyphicon glyphicon-ok">';
                    $("#"+btnId)[0].innerHTML = "删除权限";
                    $("#"+btnId).unbind();
                    console.info("移除btn的所有绑定事件");
                    $("#"+btnId).on("click", deleteAuth);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("添加管理权限失败！"+JSON.stringify(e));
            }
        })
    }

    $.when(d1,d2).done(function (v1,v2) {
        console.info("deferred info  "+"  "+v1+"   "+v2);
        var autholist = JSON.parse(author);
        for(var i=0;i<authoritys.length;i++){
            var aid = authoritys[i].id;
            var adata = authoritys[i].data;
            var dataType="用户";
            var aoperation = authoritys[i].operation;
            var operatorType = "查看";
            var hasAuth=false;
            var buttonID=aid;
            switch (adata){
                case "0":
                    dataType = "用户";
                    break;
                case "1":
                    dataType = "商户";
                    break;
                case "2":
                    dataType = "商品";
                    break;
                case "3":
                    dataType = "团购";
                    break;
                case "4":
                    dataType = "订单";
                    break;
                case "5":
                    dataType = "消息";
                    break;
                case "6":
                    dataType = "快递管理";
                    break;
                case "7":
                    dataType = "评论管理";
                    break;
            }
            switch (aoperation){
                case "0":
                    operatorType = "查看";
                    break;
                case "1":
                    operatorType = "新增";
                    break;
                case "2":
                    operatorType = "修改";
                    break;
                case "3":
                    operatorType = "删除";
                    break;
                case "4":
                    operatorType = "升降级";
                    break;
                case "5":
                    operatorType = "上下线";
                    break;
                case "6":
                    operatorType = "回复";
                    break;
                case "7":
                    operatorType = "订单处理";
                    break;
            }
            for(var j=0;j<autholist.length;j++){
                if(autholist[j]==aid||autholist[j]==""+aid){
                    hasAuth=true;
                    break;
                }
            }
            var newTr = '<tr>' +
                '<td>'+dataType+'</td>'+
                '<td>'+operatorType+'</td>';
            if(hasAuth){
                newTr += '<td id="has_'+buttonID+'"><span class="glyphicon glyphicon-ok"></td>'+
                    '<td><button id="'+buttonID+'">删除权限</button></td></tr>'
            }
            else{
                newTr += '<td id="has_'+buttonID+'"></td>'+
                    '<td><button id="'+buttonID+'">添加权限</button></td></tr>'
            }
            $("#authorList tbody").append(newTr);
            if(hasAuth){
                $("#"+buttonID).on("click", deleteAuth);
            }else{
                $("#"+buttonID).on("click", addAuth);
            }
        }

    })


})