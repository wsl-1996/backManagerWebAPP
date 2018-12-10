var userslength,content;
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
                console.log('获取users 成功')
                var users = data["data"]["users"];
                userslength=users.length;
                $("#usersList tbody").html("");
                for(var i=0;i<users.length;i++){
                    appendTr(users[i]);
                    console.log('调用appendTr 成功')
}
            }
        },
        error:function (e) {
            console.error(JSON.stringify(e));
            alert("获取用户失败！"+JSON.stringify(e));
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
        +'      </tr>';
    $("#usersList tbody").append(s);
}

function sendredpacket() {
    var redPacket={
        sum:4.3,
        explain:"送您一份中秋节红包，让您不剁手也能买买买"
    }
    var messageData = {
        messageFrom:"00000000000000000000000000000000",
        messageTo:"69d3608cb7cf437db0dd9556f20afa89",
        messageContent:JSON.stringify(redPacket),
        contentType:"0",
        messageType:"4"
    };
    $.ajax({
        url:"/ketuan/backmanage/sendBackMessage",
        type:"GET",
        data:{
            data:JSON.stringify(messageData)
        },
        dataType: "json",
        success:function (data) {
            console.info(JSON.stringify(data));
            if(data["failed"]){
                alert("红包发送失败："+data["failedMessage"]);
            }
            else{
                alert("红包发送成功");
            }
        },
        error:function (e) {
            console.error(JSON.stringify(e));
            alert("红包发送失败："+JSON.stringify(e));
        }
    })
}

function getAllGroups(returnData) {
    if(returnData==null){
        return;
    }
    $.ajax({
        url:"/ketuan/backmanage/grouplistall",
        type:"GET",
        dataType: "json",
        success:function (data) {
            console.info(JSON.data);
            if(data["failed"]){
                alert("获取团购列表失败！"+data["failedMessage"]);
            }
            else{
                console.log('获取users 成功')
                var groups = data["data"]["groups"];
                if(groups==null||groups.length==0){
                    console.info("没有获取到团购列表")
                }
                else{
                    console.info(groups);
                    returnData(groups);
                }
            }
        },
        error:function (e) {
            console.error(JSON.stringify(e));
            alert("获取团购列表失败！"+JSON.stringify(e));
        }
    })
}