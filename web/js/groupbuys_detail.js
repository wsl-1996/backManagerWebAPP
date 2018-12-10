$(document).ready(function() {
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    var groupid = getQueryString("groupid");
    $.ajax({
        url : "/ketuan/backmanage/getgroupinfo?groupid="+groupid,//请求地址
        dataType : "json",//数据格式
        type : "get",//请求方式
        async : false,//是否异步请求
        success : function(data) {   //如何发送成功
            var html = "";
            var group = data["data"]["groupdetails"];
            $("#group_name").html(group["groupName"]);
            $("#group_count").html(group["groupCount"]);
            $("#group_price").html(group["groupPrice"]);
            $("#group_type").html(group["groupType"]);
            $("#group_id").html(group["id"]);
            $("#group_userId").html(group["userId"]);
            $("#group_productId").html(group["productId"]);
            $("#group_offeredCount").html(group["offeredCount"]);
            $("#group_offeredUserId").html(group["offeredUserId"]);
            $("#group_groupState").html(group["groupState"]);
            $("#group_endTime").html(group["endTime"]);
            $("#group_customerService").html(group["customerService"]);
            $("#group_groupDiscription").html(group["groupDiscription"]);
            $("#group_deliverCity").html(group["deliverCity"]);
            // for(var i=0;i<data.length;i++){    //遍历data数组
            //     var ls = data[i];
            //     html +="<li><a href='second page text.html?newsid="+ls.news_id+"'class='infNews_wrod_a'><span>"+ls.news_name+"</span></a><span class='date'>"+ls.news_time+"</span></li>";
            // }
            // $("#ulul").html(html); //在html页面id=ulul的标签里显示html内容
        },
        error:function (e) {
            console.error(e.toString());
        }
    })
})