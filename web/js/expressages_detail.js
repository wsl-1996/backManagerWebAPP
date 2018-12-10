$(document).ready(function() {
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    var expressageid = getQueryString("expressageid");
    $.ajax({
        url : "/ketuan/backmanage/getexpressagedetails?expressageid="+expressageid,//请求地址
        dataType : "json",//数据格式
        type : "get",//请求方式
        async : false,//是否异步请求
        success : function(data) {   //如何发送成功
            var html = "";
            var expressage = data["data"]["expressages"];
            $("#expressage_name").html(expressage["expressageName"]);
            $("#expressage_Price").html(expressage["expressagePrice"]);
            $("#expressage_addressOfSevvice").html(expressage["addressOfSevvice"]);
            $("#expressage_shipAddress").html(expressage["shipAddress"]);
            $("#expressage_id").html(expressage["id"]);
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