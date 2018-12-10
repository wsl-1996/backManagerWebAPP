$(function(){
    $("#logoutBtn").on("click",function () {
        $.ajax({
            url:"/ketuan/backmanage/logout",
            type:"POST",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("退出登录失败！"+data["failedMessage"]+".关闭浏览器30分钟后系统自动退出登录。");
                }
                else{
                    window.location.href='./login.html';
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("退出登录失败！"+JSON.stringify(e)+".关闭浏览器30分钟后系统自动退出登录。");
            }
        })
    })
})