$(function(){
    $.ajax({
        url:"/ketuan/backmanage/checkli",
        type:"get",
        dataType: "json",
        success:function (data) {
            console.info(JSON.stringify(data));
            if(data["failed"]){
                window.location.href='./login.html';
            }
            else{
                var responseData = data["data"];
                var sessionId = responseData["sessionId"];
                var authorid = responseData["authorid"];
                logined=true;
                sessionStorage.setItem("logined",true);
                sessionStorage.setItem("sessionId",sessionId);
                sessionStorage.setItem(sessionId,authorid);
            }
        },
        error:function (e) {
            console.error(JSON.stringify(e));
            window.location.href='./login.html';
        }
    })


})