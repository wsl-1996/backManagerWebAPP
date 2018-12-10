$(function(){
    function getAllAuthDefine(){
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
                    initAuthDefine(authoritys)
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取权限定义信息失败！"+JSON.stringify(e));
            }
        })
    }

    function initAuthDefine(authoritys) {
        for(var i=0;i<authoritys.length;i++){
            var aid = authoritys[i].id;
            var adata = authoritys[i].data;
            var dataType="用户";
            var aoperation = authoritys[i].operation;
            var operatorType = "查看";
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
            var newTr = '<tr>' +
                '<td><label>' +
                '      <input type="checkbox" id="checkAuth_'+aid+'">' +
                '    </label></td>'+
                '<td>'+dataType+'</td>'+
                '<td>'+operatorType+'</td>'+
                '</tr>';
            $("#authorList tbody").append(newTr);
        }
    }
    getAllAuthDefine();
    
    $("#createBtn").on("click",function () {
        var admin_count = $("#authority_count")[0].value;
        var admin_name = $("#authority_name")[0].value;
        var admin_user_name = $("#author_user_name")[0].value;
        var admin_user_phone = $("#author_user_phone")[0].value;
        if(admin_count==null||admin_count==""){
            alert("请填写管理员账号");
            return;
        }
        if(admin_name==null||admin_name==""){
            alert("请填写管理员展示名称");
            return;
        }
        if(admin_user_name==null||admin_user_name==""){
            alert("请填写管理员真实姓名");
            return;
        }
        if(admin_user_phone==null||admin_user_phone==""){
            alert("请填写管理员真实联系电话");
            return;
        }
        var table =document.getElementById("authorList");
        var rowsCount = table.rows.length-1;
        var auths = [];
        for(var i=0;i<rowsCount;i++){
            var id = i+1;
            var checkid="checkAuth_"+id;
            var checked = $("#"+checkid)[0].checked;
            if(checked){
                auths.push(id);
            }
        }
        if(auths.length==0){
            alert("没有赋予管理员权限，请勾选相应的权限");
            return;
        }
        $.ajax({
            url:"/ketuan/backmanage/addmanager",
            type:"POST",
            dataType: "json",
            data:{
                admin_count:admin_count,
                admin_name:admin_name,
                admin_user_name:admin_user_name,
                admin_user_phone:admin_user_phone,
                auths:auths
            },
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("创建管理员失败！"+data["failedMessage"]);
                }
                else{
                    var authorityId = data["data"]["authorityId"];
                    var count = data["data"]["count"];
                    var pass = data["data"]["pass"];
                    console.info("成功创建管理员：Id:"+authorityId+"账户："+count+"密码："+pass);
                    alert("成功创建管理员：账户："+count+"密码："+pass+"\n请牢记管理员账号密码，遗失不补");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("创建管理员失败！"+JSON.stringify(e));
            }
        })

    })
})