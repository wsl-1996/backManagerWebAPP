$(function(){
    $("#createBtn").on("click",function (e) {
        console.info("创建商家");
        var merchant_name = $("#merchant_name")[0].value;
        var merchant_add = $("#merchant_add")[0].value;
        var merchant_phone = $("#merchant_phone")[0].value;
        var merchant_info = $("#merchant_info")[0].value;
        var merchant_count = $("#merchant_count")[0].value;
        var merchant_pass = $("#merchant_pass")[0].value;
        var merchant_pass_re = $("#merchant_pass_re")[0].value;
        if(merchant_name==undefined||merchant_name==""){
            alert("请填写商户名");
        }
        if(merchant_add==undefined||merchant_add==""){
            alert("请填写商户的联系地址");
        }
        if(merchant_phone==undefined||merchant_phone==""){
            alert("请填写商户联系方式");
        }
        if(merchant_count==undefined||merchant_count==""){
            alert("请填写商户的登陆账号");
        }
        if(merchant_pass!=merchant_pass_re){
            alert("商户的登陆密码不一致");
        }
        $.ajax({
            url:"/ketuan/backmanage/createMerchant",
            type:"POST",
            data:{
                "merchant_name":merchant_name,
                "merchant_add":merchant_add,
                "merchant_phone":merchant_phone,
                "merchant_info":merchant_info,
                "merchant_count":merchant_count,
                "merchant_pass":merchant_pass
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("商家添加失败！"+data["failedMessage"]);
                }
                else{
                    alert("商家添加成功！");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商家添加失败！"+JSON.stringify(e));
            }

        })
    })

})
