$(function(){

    var ws = new WebSocket("ws://www.skqtec.com:8080/ketuan/websocket");
    var interval;
    var messageCountInterval;
    ws.onopen = function(evt) {
        console.log("Connection open ...");
        interval=window.setInterval(sendConnect,1000);
        localStorage.setItem("newCommingMessage",JSON.stringify({}));
        messageCountInterval = window.setInterval(changeMessageCount,1000);

    };

    function changeMessageCount() {
        var newMessage = JSON.parse(localStorage.getItem("newCommingMessage"));
        var count = 0;
        for (var userid in newMessage){
            var useMessageList = newMessage[userid];
            if(useMessageList!=null&& useMessageList.length>0){
                for(var i=0;i<useMessageList.length;i++){
                    if(!useMessageList[i]["readMark"]){
                        count++;
                    }
                }
            }
        }
        $("#newMessageCount")[0].innerHTML=count;
        $("#newCommingMessageCountBell")[0].innerHTML=count;

    }
    
    function sendConnect(){
        if(sessionStorage.getItem("logined")){
            var data = {};
            data["messageFrom"] = "00000000000000000000000000000000";
            data["messageType"]="-1";
            ws.send(JSON.stringify(data));
            window.clearInterval(interval);
        }
        else {

        }
    }

    ws.onerror = function(evt) {
        console.error(evt);
    };

    ws.onmessage = function(evt) {
        console.log( "Received Message: " + evt.data);
        var commingMessage = JSON.parse(localStorage.getItem("newCommingMessage"));
        var message = JSON.parse(evt.data);
        message["readMark"]=false;
        var messageFrom = message["messageFrom"];
        var userMessageList = commingMessage[messageFrom];
        if(userMessageList==null){
            userMessageList=[];
            userMessageList.push(message);
        }
        else {
            userMessageList.push(message);
        }
        commingMessage[messageFrom] = userMessageList;
        localStorage.setItem("newCommingMessage",JSON.stringify(commingMessage));
    };

    ws.onclose = function(evt) {
        console.log("Connection closed.");
    };
})