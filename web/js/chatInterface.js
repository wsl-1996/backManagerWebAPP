// $(function(){
    var currentInterval;
    var sessionId = sessionStorage.getItem("sessionId");
    var currentAuthorid = sessionStorage.getItem(sessionId);

    var current_userid, current_mesageType;
    /**
     * 审核通过发送消息
     */
    var passCheck = function (messageId,userId) {
        var message = getMessage(messageId,userId);
        var type = message["messageType"];
        var messageFrom = message["messageFrom"];
        var content ="";
        switch (type){
            case "1":
                content="尊敬的用户，您好：您的用户升级请求，系统已审核通过，现在您已有更高级别的用户权利，赶快去我的用户里看看吧。";
                break;
            case "2":
                content="尊敬的用户，您好：您的退款请求，系统已审核通过，退款金额将于15个工作日内，返回至您的账户。感谢您对客团的支持与理解。";
                break;
            case "3":
                content="尊敬的用户，您好：您的提现请求，系统已审核通过，提现金额将于3个工作日内，进入您的银行账户，请您及时关注自己的账户情况。";
                break;
            case "6":
                content="尊敬的用户，您好：您投诉的问题，后台已经完成审核。我们将对相应的问题进行处理，感谢您对客团的监督与支持。";
                break;
        }
        sendMessage(currentAuthorid,messageFrom,content,"0","5");
        mesageHandeled(messageId);
        removeViewedMessageFromLocalStorage(message);
    }

    /**
     * 审核不通过发送消息
     */
    var notPassCheck =function (messageId,userId) {
        var message = getMessage(messageId,userId);
        var type = message["messageType"];
        var messageFrom = message["messageFrom"];
        var content ="";
        switch (type){
            case "1":
                content="尊敬的用户，您好：您的用户升级请求，系统审核未通过，请详细查看用户等级权利说明，或致电客服详询。";
                break;
            case "2":
                content="尊敬的用户，您好：您的退款请求，系统审核未通过，审核未通过原因请致电客服详询。";
                break;
            case "3":
                content="尊敬的用户，您好：您的提现请求，系统审核未通过，审核未通过原因请致电客服详询。";
                break;
            case "6":
                content="尊敬的用户，您好：您投诉的问题，后台已经完成审核。我们将对相应的问题进行处理，感谢您对客团的监督与支持。";
                break;
        }
        sendMessage(currentAuthorid,messageFrom,content,"0","5");
        mesageHandeled(messageId);
        removeViewedMessageFromLocalStorage(message);
    }
    var adminHeadImgUrl = SystemConfig.webBaseUrl+"img/officaltalkicon.svg";
    $("#sendCustomMessage").on("click",function () {
        var content = $("#sendCustomMessageContent").val();
        var messageData = {
            messageFrom:currentAuthorid,
            messageTo:current_userid,
            messageContent:content,
            headOwner:adminHeadImgUrl,
            contentType:"0",
            messageType:"0"
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
                    alert("消息发送失败："+data["failedMessage"]);
                }
                else{
                    appendCusstomMessageDetail(content,true);
                    $("#sendCustomMessageContent").value="";
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("消息发送失败："+JSON.stringify(e));
            }
        })
    })
    
    var customMessage = '<div class="left">\n' +
        '                            <div class="author-name">\n' +
        '                                replace_userid <small class="chat-date">\n' +
        '                                replace_time' +
        '                            </small>\n' +
        '                            </div>\n' +
        '                            <div class="chat-message active">\n' +
        '                                replace_content' +
        '                            </div>\n' +
        '                        </div>';

    var customImgMessage = '<div class="left">\n' +
        '                            <div class="author-name">\n' +
        '                                replace_userid <small class="chat-date">\n' +
        '                                replace_time' +
        '                            </small>\n' +
        '                            </div>\n' +
        '                            <img class="chat-message active">\n' +
        '                                <img width="200px" src="replace_img_url"></img>' +
        '                            </div>\n' +
        '                        </div>';

    var myCustomMessage = '<div class="right">\n' +
        '                            <div class="author-name">\n' +
        '                                客服\n' +
        '                                <small class="chat-date">\n' +
        '                                    replace_time' +
        '                                </small>\n' +
        '                            </div>\n' +
        '                            <div class="chat-message">\n' +
        '                                replace_content' +
        '                            </div>\n' +
        '                        </div>';

    var systemMessageForUpdate="<div class=\"mail-box\">\n" +
        "                    <div class=\"mail-body\">\n" +
        "                        <p>用户  replace_userid  replace_content</p>\n" +
        "                    </div>\n" +
        "                <div class=\"mail-body text-right tooltip-demo\">\n" +
        "                        <button  class=\"btn btn-sm btn-white \" id=\"notpasscheckBtn\"  messageId=\"replace_messageid\" userId=\"replace_userid\"><i class=\"fa fa-check\"></i> 审核不通过</button>\n" +
        "                        <button  class=\"btn btn-sm btn-white \" id=\"checkpassBtn\" messageId=\"replace_messageid\" userId=\"replace_userid\"><i class=\"fa fa-close\"></i> 审核通过</button>\n" +
        "                </div>"
        "           </div>";


    function openMessageDetail(userid, messageType){
        current_userid=userid;
        current_mesageType=messageType;
        console.info("userid:"+userid+"   messageType:"+messageType);
        if(messageType=="0"){
            $("#systemMessageBox").hide();
            $("#customMessageBox").show();
            $("#customMessageContentBox")[0].innerHTML="";
        }
        else{
            $("#systemMessageBox").show();
            $("#customMessageBox").hide();
            if(current_userid!=userid){
                $("#systemMessageBox")[0].innerHTML="";
            }
        }
        getUserInfo(userid);
        appendMessageDetail(userid,messageType);
        if(currentInterval){
            window.clearInterval(currentInterval);
        }
        currentInterval = window.setInterval(newCommingMessageHandle,1000);
    }

    function newCommingMessageHandle() {
        if(current_mesageType==0){
            appendMessageDetail(current_userid,current_mesageType);
        }
    }
    
    function removeViewedMessageFromLocalStorage(message) {
        var newCommingMessage = JSON.parse(localStorage.getItem("newCommingMessage"));
        var userid = message["messageFrom"];
        var userMessageList = newCommingMessage[userid];
        for(var i=0;i<userMessageList.length;i++){
            if(userMessageList[i]["id"]==message["id"]){
                userMessageList[i]["readMark"] = true;
                break;
            }
        }
        newCommingMessage[userid] = userMessageList;
        localStorage.setItem("newCommingMessage",JSON.stringify(newCommingMessage));
    }

    function appendMessageDetail(userid,mesageType){
        var newCommingMessage = JSON.parse(localStorage.getItem("newCommingMessage"));
        var userMessageList = newCommingMessage[userid];
        var showMessage=[];
        for(var i=0;i<userMessageList.length;i++){
            if(mesageType==userMessageList[i]["messageType"]&&!userMessageList[i]["readMark"]){
                showMessage.push(userMessageList[i]);
            }
        }
        if(mesageType=="0"){
            //$("#customMessageContentBox").innerHTML="";
            for(var j=0;j<showMessage.length;j++){
                appendCusstomMessageDetail(showMessage[j],false);
                removeViewedMessageFromLocalStorage(showMessage[j]);
            }
        }
        else {
            //$("#systemMessageBox").innerHTML = "";
            for(var j=0;j<showMessage.length;j++){
                appendSystemMessageDetail(showMessage[j]);
            }
        }
        var height = $("#customMessageContentBox")[0].offsetHeight;
        $(".slimScrollDiv")[0].scrollTo(0,height);
    }

    function appendCusstomMessageDetail(message,isMy) {
        var myMessage="";
        if(!isMy){
            var contentType = message["contentType"];
            var createTime = message["createTime"];
            var content = message["messageContent"];
            if(contentType=="0"){
                myMessage = customMessage.replace("replace_content",content);
            }
            else{
                myMessage = customImgMessage.replace("replace_img_url",content);
            }
            myMessage = myMessage.replace("replace_time",createTime);
        }
        else{
            myMessage = myCustomMessage.replace("replace_content",message);
            myMessage = myMessage.replace("replace_time",getNowDate());

        }

        $("#customMessageContentBox").append(myMessage);
    }

    function appendSystemMessageDetail(message) {
        var newMessage=systemMessageForUpdate.replace("replace_userid",current_userid);
        newMessage = newMessage.replace("replace_messageid",message["id"]);
        newMessage = newMessage.replace("replace_messageid",message["id"]);
        newMessage = newMessage.replace("replace_userid",message["messageFrom"]);
        var type = message["messageType"];
        var content="";
        switch (type){
            case 1:
                content = "请求升级";
                break;
            case 2:
                content = "申请退款";
                break;
            case 3:
                content = "申请提现";
                break;
            case 6:
                content = "投诉";
                break;
        }
        newMessage = newMessage.replace("replace_content",content);
        $("#systemMessageBox").append(newMessage);

        $("#notpasscheckBtn").on("click",function () {
            console.info("审核不通过");
            var messageid = $(this).attr("messageId");
            var userid = $(this).attr("userId");
            notPassCheck(messageid,userid);
        })

        $("#checkpassBtn").on("click",function () {
            console.info("审核通过");
            var messageid = $(this).attr("messageId");
            var userid = $(this).attr("userId");
            passCheck(messageid,userid);
        })

    }

    function getUserInfo(userid) {
        $.ajax({
            url : "/ketuan/backmanage/usergetdetail?userid="+userid,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            success : function(data) {   //如何发送成功
                var user = data["data"]["user"];
                $("#userid").html(userid);
                $("#nickname").html(user["nickname"]);
                $("#phone").html(user["phone"]);
                $("#email").html(user["email"]);
                $("#balance").html(user["balance"]);
            },
            error:function (e) {
                console.error(e.toString());
            }
        })
    }
    
    function openCustomMessageDetail() {
        var currentId = $(this).attr("id");
        openMessageDetail(currentId,0);
    }
    
    function openSystemMessageDetail() {
        var currentId = $(this).attr("id");
        var type = $(this).attr("messageType");
        openMessageDetail(currentId,type);
    }

    function refreshMessageList(){
        $("#customMessageList tbody").html("<tr>\n" +
            "                                <td>  </td>\n" +
            "                                <td>用户id</td>\n" +
            "                                <td>未读消息数</td>\n" +
            "                            </tr>");
        $("#systemMessageList tbody").html("<tr>\n" +
            "                                <td>  </td>\n" +
            "                                <td>用户id</td>\n" +
            "                                <td>未处理信息数</td>\n" +
            "                            </tr>");
        var newMessage = JSON.parse(localStorage.getItem("newCommingMessage"));
        var newMessageUser = [];
        var newMessageUserSystem = [];
        for (var userid in newMessage){
            var userMessage=newMessage[userid];
            if(userMessage==null||userMessage.length==0){
                continue;
            }
            var headOwner = userMessage[0]["headOwner"];
            var newUser = {};
            newUser["userid"] = userid;
            newUser["headImgUrl"] = headOwner;
            newUser["count"] = 0;
            var newSystemUser = {};
            newSystemUser["userid"] = userid;
            newSystemUser["headImgUrl"] = headOwner;
            newSystemUser["count"] = 0;
            for(var i=0;i<userMessage.length;i++){
                var messageType = userMessage[i]["messageType"];
                if(!userMessage[i]["readMark"]){
                    if(messageType==0){
                        newUser["messageType"]=messageType;
                        newUser["count"]+=1;
                    }
                    else {
                        newSystemUser["messageType"]=messageType;
                        newSystemUser["count"]+=1;
                    }
                }
            }
            newMessageUser.push(newUser);
            newMessageUserSystem.push(newSystemUser);
        }
        for(var j=0;j<newMessageUser.length;j++){
            var line = '<tr class="read" id="'+newMessageUser[j]["userid"]+'"><td><img style="width: 30px;height: 30px" src="'+newMessageUser[j]["headImgUrl"]+'"></td><td>'+newMessageUser[j]["userid"]+'</td><td>'+newMessageUser[j]["count"]+'</td></tr>'
            $("#customMessageList tbody").append(line);
        }
        for(var k=0;k<newMessageUserSystem.length;k++){
            var line = '<tr class="read" id="'+newMessageUserSystem[k]["userid"]+'" messageType="'+newMessageUserSystem[k]["messageType"]+'"><td><img style="width: 30px;height: 30px" src="'+newMessageUserSystem[k]["headImgUrl"]+'"></td><td>'+newMessageUserSystem[k]["userid"]+'</td><td>'+newMessageUserSystem[k]["count"]+'</td></tr>'
            $("#systemMessageList tbody").append(line);
        }
        $("#customMessageList tbody tr").on("click", openCustomMessageDetail);
        $("#systemMessageList tbody tr").on("click", openSystemMessageDetail);
    }
    window.setInterval(refreshMessageList,1000);

    /**
     * 系统消息处理时，查看用户的详细信息，用模态框打开，查看用户的消费和订单信息
     */
    function showUserDetail() {

    }

    /**
     * 获取历史客服消息
     */
    function getHistoryCustomMessage(){

    }

    /**
     * 获取未处理的系统消息
     */
    function getUnhandledSystemMessage(){

    }

    /**
     * 获取历史系统消息
     */
    function getHistorySystemMessage(){

    }

    function getMessage(messageId,userId) {
        var newCommingMessage = JSON.parse(localStorage.getItem("newCommingMessage"));
        var userMesageList = newCommingMessage[userId];
        for(var i=0;i<userMesageList.length;i++){
            if(userMesageList[i]["id"]==messageId){
                return userMesageList[i];
            }
        }
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
            url:"/ketuan/backmanage/sendBackMessage",
            type:"GET",
            data:{
                data:JSON.stringify(messageData)
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("消息发送失败："+data["failedMessage"]);
                }
                else{
                    alert("消息发送成功");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("消息发送失败："+JSON.stringify(e));
            }
        })
    }

    function mesageHandeled(messageId) {
        $.ajax({
            url:"/ketuan/backmanage/handledMessage",
            type:"GET",
            data:{
                messageid:messageId
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("更新消息状态失败："+data["failedMessage"]);
                }
                else{
                    alert("消息状态更新成功");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("更新消息状态失败："+JSON.stringify(e));
            }
        })
    }
// })
