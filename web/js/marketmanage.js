function removeBanner(bannerId){
    $.ajax({
        url:"/ketuan/backmanage/removebanner?id="+bannerId,
        type:"GET",
        dataType: "json",
        success:function (data) {
            console.info(JSON.stringify(data));
            if(data["failed"]){
                alert("移除banner失败1："+data["failedMessage"]);
            }
            else{
                alert("移除banner成功");
            }
        },
        error:function (e) {
            console.error(JSON.stringify(e));
            alert("移除banner失败："+JSON.stringify(e));
        }
    })
}

var selected=false;
function selectGroup(groupId){
    $("#selectedGroup")[0].innerHTML = groupId;
    $("#deleteSelected").show();
}
window.onload=function () {
    getAllUsers();

    $("#deleteSelected").on("click",function (data) {
        $("#selectedGroup")[0].innerHTML = "";
        $("#deleteSelected").hide();
    })

    getAllGroups(appendGroups);

    var groupListOpend = false;
    $("#openGroupList").on("click",function () {
        if(groupListOpend){
            $("#groupsList").hide();
            groupListOpend = false;
            $("#openGroupList")[0].innerHTML="展开团购列表";
        }
        else{
            $("#groupsList").show();
            groupListOpend = true;
            $("#openGroupList")[0].innerHTML="关闭团购列表";
        }
    })
    
    function appendGroups(groups){
        $("#groupsList tbody").html("");
        for(var i=0;i<groups.length;i++){
            var group = groups[i];
            appendGroup(group);
        }
    }
    function appendGroup(group){
        var stateString = "未上线";
        var stateLabel = "label-warning";
        var groupId = group["id"];
        var state = group["groupState"];
        switch (state){
            case 1:
                stateString="已上线";
                stateLabel = "label-primary";
                break;
            case 2:
                stateString="已下线";
                break;
        }
        var endTimeStamp = group["endTime"];
        var endTime = formatDateTime(endTimeStamp);
        var name = group["groupName"];
        var imgUrl = group["groupFirstImg"];
        var s = '<tr id="'+groupId+'">' +
            '<td><span class="label '+stateLabel+'">'+stateString+'</span></td>' +
            '<td><img style="width: 30px;height: 30px" src="'+imgUrl+'"></td>' +
            '<td>'+name+'</td>' +
            '<td>结束时间：'+endTime+'</td>' +
            '<td><button onclick="selectGroup(\''+groupId+'\')">选中</button></td>'
            '</tr>'
        $("#groupsList tbody").append(s);
    }

    $('#chouseall').on('click',function () {                  // 全选按钮事件
        for (let i=0;i<userslength;i++){
            $('table input')[i].checked=!$('table input')[i].checked;
        }
    })
    
    $('#send_red_btn').on('click',function () {
        var redmoney=$('#moneyinput')[0].value
        var redexplain=$('#explaininput')[0].value
        var msgarr=[]
        for (let i=0;i<userslength;i++){
            if ($('table input')[i].checked==true){
                var msgto=$('table input')[i].value
                msgarr.push(msgto)
            }
        }
        console.log('allsend',msgarr)
        var redPacket={
            sum:redmoney,
            explain:redexplain
        }
        var messageData = {
            messageFrom:"00000000000000000000000000000000",
            messageTo:JSON.stringify(msgarr),
            messageContent:JSON.stringify(redPacket),
            contentType:"0",
            messageType:"4"
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
                    alert("红包发送失败1："+data["failedMessage"]);
                }
                else{
                    alert("红包发送成功");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("红包发送失败2："+JSON.stringify(e));
            }
        })
    })
    
    function getAllBanners() {
        $.ajax({
            url:"/ketuan/backmanage/bannerlistall",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.data);
                if(data["failed"]){
                    alert("获取首页滑动内容列表失败！"+data["failedMessage"]);
                }
                else{
                    var banners = data["data"]["banners"];
                    if(banners==null||banners.length==0){
                        console.info("没有获取到首页滑动内容列表");
                    }
                    else{
                        console.info(banners);
                        appendBaners(banners);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取首页滑动内容列表失败！"+JSON.stringify(e));
            }
        })
    }

    function appendBaners(banners){
        $("#bannersList tbody").html("");
        for(var i=0;i<banners.length;i++){
            appendBaner(banners[i]);
        }
    }
    function appendBaner(banner){
        var imgUrl = banner["imgUrl"];
        var id = banner["id"];
        var canTab = banner["canTab"];
        var groupName = banner["groupName"];
        var discript = groupName;
        if(!canTab){
            discript='<label class="label label-info">纯图片</label>';
        }
        var s = '<tr id="'+id+'">' +
            '<td><img src="'+imgUrl+'" style="width: 40px;height: 40px"></td>' +
            '<td>'+discript+'</td>' +
            '<td><button onclick="removeBanner(\''+id+'\')">移除</button></td>' +
            '</tr>';
        $("#bannersList tbody").append(s);
    }
    getAllBanners();

    $("#createNewBanner").on("click",function (data) {
        var selectedGroupid = $("#selectedGroup")[0].innerHTML;
        var canTab = 1;
        if(selectedGroupid==null||selectedGroupid==""){
            canTab=0;
        }
        var imgUrl = firstImg;
        $.ajax({
            url:"/ketuan/backmanage/createbanner",
            type:"POST",
            data:{
                imgUrl:imgUrl,
                canTab:canTab,
                groupId:selectedGroupid
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.data);
                if(data["failed"]){
                    alert("创建banner失败！"+data["failedMessage"]);
                }
                else{
                    alert("创建banner成功，刷新页面查看最新！");
                    // var banners = data["data"]["banners"];
                    // if(banners==null||banners.length==0){
                    //     console.info("没有获取到首页滑动内容列表");
                    // }
                    // else{
                    //     console.info(banners);
                    //     appendBaners(banners);
                    // }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("创建banner失败！"+JSON.stringify(e));
            }
        })
    })
}
