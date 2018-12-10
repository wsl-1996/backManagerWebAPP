$(function(){
    $("#loginBtn").on("click",function(){
        var username = $("#user_name")[0].value;
        var pass = $("#pass")[0].value;
        if(username==null||username==""){
            alert("请输入用户名");
            return;
        }
        if(pass==null||pass==""){
            alert("请输入密码");
            return;
        }
        var base = new Base64();
        var pass_encode = base.encode(pass);
        $.ajax({
            url:"/ketuan/backmanage/login",
            type:"POST",
            dataType: "json",
            data:{
                username:username,
                pass:pass_encode
            },
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("登录失败！"+data["failedMessage"]);
                }
                else{
                    console.log('登陆返回',data.data)
                    var authorityId=data.data.author.authorityId
                    window.location.href='./welcome.html?authorid='+authorityId;
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("登录失败！"+JSON.stringify(e));
            }
        })
    })
})