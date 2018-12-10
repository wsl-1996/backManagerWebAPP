    $(document).ready(function() {
        function getQueryString(name) {
            var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
            var r = window.location.search.substr(1).match(reg);
            if (r != null) return unescape(r[2]); return null;
        }
        var merchantid = getQueryString("merchantid");
        $.ajax({
            url : "/ketuan/backmanage/merchantgetdetail?merchantid="+merchantid,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            async : false,//是否异步请求
            success : function(data) {   //如何发送成功
                var html = "";
                var merchant = data["data"]["merchant"];
                $("#merchant_name").html(merchant["name"]);
                $("#merchant_discription").html(merchant["discription"]);
                $("#merchant_phone").html(merchant["phone"]);
                $("#merchant_address").html(merchant["address"]);
                $("#merchant_accountname").html(merchant["accountname"]);
                $("#merchant_headImgUrl").html(merchant["headImgUrl"]);
            },
            error:function (e) {
                console.error(e.toString());
            }
        })
    })
