$(document).ready(function() {
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    var messageid = getQueryString("messageid");
    $.ajax({
        url : "/ketuan/backmanage/getMessageInfo?messageid="+messageid,//请求地址
        dataType : "json",//数据格式
        type : "get",//请求方式
        async : false,//是否异步请求
        success : function(data) {   //如何发送成功
            var html = "";
            var message = data["data"]["messagedetails"];
            $("#message_content").html(message["content"]);
            $("#message_from_user").html(message["fromUserId"]);
        },
        error:function (e) {
            console.error(e.toString());
        }
    })
})