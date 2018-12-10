$(document).ready(function() {
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    function usergradehandler(grade) {
        if (grade==0){
            return '普通会员'
        } else if (grade==1){
            return '合伙人'
    } else {
            return '超级合伙人'
        }
    }

    function usersexhandler(sex) {
        if (sex==0){
            return '男'
        } else if (sex==1){
            return '女'
        } else {
            return '未知'
        }
    }



    function appendfans(children) {
        for (let i=0;i<children.length;i++){
            var headimgurl=children[i].head_img_url
            var cid=children[i].id
            var s='<a href="users_detail.html?userid='+cid+'">\n' +
                '  <img alt="image" class="img-circle" src='+headimgurl+'>\n' +
                '  </a>'
            $('#fanslist').append(s)
        }
    }

    var userid = getQueryString("userid");
    $.ajax({
        url : "/ketuan/backmanage/usergetdetail?userid="+userid,//请求地址
        dataType : "json",//数据格式
        type : "get",//请求方式
        async : 'false',//是否异步请求
        success : function(data) {   //如何发送成功
            var html = "";
            var user = data["data"]["user"];
            var children = data['data']['childrenInfo']
            $("#user_id").html(user["id"]);
            $("#user_nickname").html(user["nickname"]);
            $('#user_grade').html(usergradehandler(user["grade"]));
            $("#user_phone").html(user["phone"]);
            $("#user_email").html(user["email"]);
            $("#user_sex").html(usersexhandler(user["sex"]));
            $("#user_city").html(user["city"]);
            $("#user_country").html(user["country"]);
            appendfans(children);
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