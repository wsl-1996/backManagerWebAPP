
$(function(){
    // var new_element = document.createElement("script");
    // new_element.setAttribute("type", "text/javascript");
    // new_element.setAttribute("src", "chatInterface.js");
    // document.body.appendChild(new_element);
    var userslength,content;

    getAllUsers();

    $('#sk-wait').css({'position':'absolute','left':'50%','top':'50%','margin-left':'-25px','margin-top':'-25px','display':'none'})

    $('#chouseall').on('click',function () {
        for (let i=0;i<userslength;i++){
            $('table input')[i].checked=!$('table input')[i].checked;
        }
    })
    
    $('#chousesend').on('click',function () {

        if (looktable()){
          $('#sendmsgmodal').modal('show')
        }else {
            alert("请选择用户")
        }
    })




    $("#loading-example-btn").on("click",function (e){
        getAllUsers();
    });

    $('#inputtext').on('change',function () {
        content=$('#inputtext')[0].value
    })

function onesendclick(){
    $('.onesend').on('click',function () {
        console.log('onesend点击事件')
        $($(this)[0]).parent().parent().find('input')[0].checked=true
        $('#sendmsgmodal').modal('show')
    })
}

function sendsucc(){                 //发送成功之后把复选框中的勾去掉
    $('#sendmsgmodal').modal('hide');
    for (let i=0;i<userslength;i++){
        $('table input')[i].checked=false;
    }
}
    $('#sendbtn').on('click',function () {
        $('#sk-wait').show()
        var msgarr=[]
        console.log('用户表长',userslength)
        console.log('消息内容',content)
        var msgtype=$('#msgtypediv input:checked')[0].value
        for (let i=0;i<userslength;i++){
            if ($('table input')[i].checked==true){
                var msgto=$('table input')[i].value
                msgarr.push(msgto)
            }
        }
        msgarr=JSON.stringify(msgarr)
        sendMessage("00000000000000000000000000000000",msgarr,content,"0",msgtype);
    })
    
    $("#searchBtn").on("click",function (e){
        var key = $("#searchKey")[0].value;
        if(key==""||key==undefined){
            getAllUsers();
            return
        }
        $.ajax({
            url:"/ketuan/backmanage/usersearch?key="+key,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("搜索用户失败！"+data["failedMessage"]);
                }
                else{
                    var users = data["data"]["users"];
                    userslength=users.length;
                        $("#usersList tbody").html("");
                    for(var i=0;i<users.length;i++){
                        appendTr(users[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("搜索用户失败！"+JSON.stringify(e));
            }
        })
    });

    function looktable() {
        for (let i=0;i<userslength;i++){
            if ($('table input')[i].checked==true){return true}

        }
    }

    function getAllUsers() {
        $.ajax({
            url:"/ketuan/backmanage/userlistall",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.data);
                if(data["failed"]){
                    alert("获取用户失败！"+data["failedMessage"]);
                }
                else{
                    var users = data["data"]["users"];
                    userslength=users.length;
                    $("#usersList tbody").html("");
                    for(var i=0;i<users.length;i++){
                        appendTr(users[i]);
                    }
                    onesendclick();
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取用户失败！"+JSON.stringify(e));
            }
        })
    }
    function sendMessage(customServiceId, userId, messageContent, contentType, messageType) {
        var messageData = {
            messageFrom:customServiceId,
            messageTo:userId,
            messageContent:messageContent,
            contentType:contentType,
            messageType:messageType
        };
        $.ajax({
            url:"/ketuan/backmanage/sendsomeBackMessage",
            type:"GET",
            data:{
                data:JSON.stringify(messageData)
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    $('#sk-wait').hide()
                    alert("消息发送失败："+data["failedMessage"]);
                }
                else{
                    sendsucc();
                    $('#sk-wait').hide()
                    alert("消息发送成功");

                }
            },
            error:function (e) {
                $('#sk-wait').hide()
                console.error(JSON.stringify(e));
                alert("消息发送失败："+JSON.stringify(e));
            }
        })
    }
    function appendTr(user){
        var sex = "男";
        switch (user["sex"]){
            case 0:
                sex="男";
                break;
            case 1:
                sex="女";
                break;
            case 2:
                sex="未知";
                break;
        }
        var s = '<tr>'
            +'          <td>'
            + '          <input type="checkbox" value='+user.id+' />'
            + '            </td>'
            +'          <td class="project-status">'
            + '             <span class="label label-primary">'+sex
            +'          </td>'
            +'          <td class="project-title">'
            +'              <img alt="image" style="width: 40px" class="img-circle" src="'+user["headImgUrl"]+'">'
            +'              <a href="users_detail.html?userid='+user["id"]+'">'+user["nickname"]
            +'              </a>'
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <small>'+user["province"]+'</small>'
            +'              <small>'+user["city"]+'</small>'
            +'          </td>'
            +'          <td class="project-actions">'
            +'              <a href="users_detail.html?userid="'+user["id"]+' class="btn btn-white btn-sm"><i class="fa fa-folder"></i> 查看 </a>'
            +'              <a class="btn btn-white btn-sm onesend"><i class="fa fa-pencil"></i> 私信</a>'
            +'          </td>'
            +'      </tr>';
        $("#usersList tbody").append(s);
    }


});
