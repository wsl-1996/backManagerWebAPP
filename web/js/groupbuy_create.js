$(function(){

    //商品产地地址的获取
    $("#distpicker").on("change",function (data) {
        var address_before1=$("#groupbuy_deliver_add").val();
        var province1= $('#province1 option:selected').val();
        var city1= $('#city1 option:selected').val();
        var district1= $('#district1 option:selected').val();
        $("#groupbuy_deliver_add").val(address_before1+province1+city1+district1);

        var province2= $('#province1 option:selected').val();
        var city2= $('#city1 option:selected').val();
        var district2= $('#district1 option:selected').val();
        $("#groupbuy_deliver_add").val(province2+city2+district2);

        //var address_before3=$("#productproduceadd").val();
        var province3= $('#province1 option:selected').val();
        var city3= $('#city1 option:selected').val();
        var district3= $('#district1 option:selected').val();
        $("#groupbuy_deliver_add").val(province3+city3+district3);

    })


    //供货人的选择和获取
    $("#ownerid").on("click",function (e) {
        $("#group_create_search").show();
        //该input标签的记忆功能取消或者不取消
        $("#ownerid").attr('placeholder',null);
    })

    $("#searchBtn").on("click", function (e) {
        var key = $("#searchKey")[0].value;
        if (key == "" || key == undefined) {
            getAllUsers();
            return
        }
        $.ajax({
            url: "/ketuan/backmanage/usersearch?key=" + key,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("搜索用户失败！请试着搜索商户！" + data["failedMessage"]);
                }
                else {
                    var users = data["data"]["users"];
                    $("#searchList tbody").html("");
                    for (var i = 0; i < users.length; i++) {
                        appendUserTr(users[i]);
                    }
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("搜索用户失败！" + JSON.stringify(e));
            }
        })
    })


    $("#searchBtn").on("click", function (e) {
        var key = $("#searchKey")[0].value;
        if (key == "" || key == undefined) {
            getAllMerchants();
            return
        }
        $.ajax({
            url: "/ketuan/backmanage/merchantsearch?key=" + key,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("搜索商家失败！" + data["failedMessage"]);
                }
                else {
                    var merchants = data["data"]["merchants"];
                    $("#searchList tbody").html("");
                    for (var i = 0; i < merchants.length; i++) {
                        appendMerchantTr(merchants[i]);
                    }
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("搜索商家失败！请试着搜索用户！" + JSON.stringify(e));
            }
        })
    })

    function getAllMerchants() {
        $.ajax({
            url: "/ketuan/backmanage/merchantlistall",
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("获取商家失败！" + data["failedMessage"]);
                }
                else {
                    var merchants = data["data"]["merchants"];
                    $("#searchList tbody").html("");
                    for (var i = 0; i < merchants.length; i++) {
                        appendMerchantTr(merchants[i]);
                    }
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("获取商家失败！" + JSON.stringify(e));
            }
        })
    }

    function getAllUsers() {
        $.ajax({
            url: "/ketuan/backmanage/userlistall",
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("获取用户失败！" + data["failedMessage"]);
                }
                else {
                    var users = data["data"]["users"];
                    $("#searchList tbody").html("");
                    for (var i = 0; i < users.length; i++) {
                        appendUserTr(users[i]);
                    }
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("获取用户失败！" + JSON.stringify(e));
            }
        })
    }

    function appendUserTr(user) {
        var sex = "男";
        switch (user["sex"]) {
            case 0:
                sex = "男";
                break;
            case 1:
                sex = "女";
                break;
            case 2:
                sex = "未知";
                break;
        }
        var s = '<tr class="user_tr" id="' + user["id"] + '">'
            + '          <td class="project-status">'
            + '             <span class="label label-primary">' + sex
            + '          </td>'
            + '          <td class="project-title">'
            + '              <img alt="image" style="width:30px" class="img-circle" src="' + user["headImgUrl"] + '">'
            + '              <a>' + user["nickname"]
            + '              </a>'
            + '          </td>'
            + '          <td class="project-completion">'
            + '              <small>' + user["province"] + '</small>'
            + '              <small>' + user["city"] + '</small>'
            + '          </td>'
            + '          <td class="project-people">'
            //+ '              <small>业绩：</small>' + user["controbution"]
            + '          </td>'
            + '      </tr>'
        /*$("#usersList tbody").html("");*/
        $("#searchList tbody").append(s);
        $("#searchList tbody tr").on("click", getid);
    }

    function getid(data) {
        var currentId = $(this).attr("id");
        $("#ownerid").val(currentId);
        $("#group_create_search").hide();
    }

    function appendMerchantTr(merchant) {
        var s = '<tr id="' + merchant["id"] + '" class="merchant_tr">'
            + '          <td class="project-status">'
            + '             <span class="label label-primary">@'
            + '          </td>'
            + '          <td class="project-title">'
            + '              <a>' + merchant["name"]
            + '              </a>'
            + '          </td>'
            + '          <td class="project-completion">'
            + '              <small>联系电话：</small>' + merchant["phone"]
            + '          </td>'
            + '         <td class="project-people">'
            + '          </td>'
            + '      </tr>'

        $("#searchList tbody").append(s);
        $("#searchList tbody tr").on("click", getid);

    }

    $("#input_groupCreate input").on("click",function () {
        var name_choose=$('input:radio[name="b"]:checked').val();
        if(name_choose==1) {
            //选择个人;
            getAllUsers();
        }
        if(name_choose==0) {
            //radio选择商家
            getAllMerchants();
        }
    })

    $("#createBtn").on("click",function (e) {
        console.info("创建团购");
        var groupbuy_name = $("#groupbuy_name")[0].value;
        var groupbuy_info = $("#groupbuy_info")[0].value;
        var productid = $("#product_id")[0].value;
        var ownerid = $("#ownerid")[0].value;
        //var productownertype = $()
        //var productownerid = $("#ownerid")[0].value;
        var groupbuy_count = $("#groupbuy_count")[0].value;
        var groupbuy_price = $("#groupbuy_price")[0].value;
        var groupbuy_cost = $("#groupbuy_cost")[0].value;
        var groupbuy_deliver_add = $("#groupbuy_deliver_add")[0].value;
        var groupbuy_end_time = $("#groupbuy_end_time")[0].value;
        var groupbuy_start_time = $("#groupbuy_start_time")[0].value;
        var group_style_string = $("#group_style")[0].value;
        var group_style = group_style_string.split("，");
        var return_cash_rate = $("#return_cash_rate")[0].value;
        var return_cash_rate_inviter = $("#return_cash_rate_inviter")[0].value;
        var customer_service_select = $("#customer_service_select").val();

        if(isNaN(return_cash_rate)){
            alert("给客户的返现比例须填数字");
            return;
        }
        if(isNaN(return_cash_rate_inviter)){
            alert("给推荐人的返现比例须填数字");
            return;
        }
        if(isNaN(groupbuy_price)){
            alert("团购价须填数字");
            return;
        }
        if(isNaN(groupbuy_cost)){
            alert("成本价须填数字");
            return;
        }
        if(isNaN(groupbuy_count)){
            alert("成团数须填数字");
            return;
        }

        $.ajax({
            url:"/ketuan/backmanage/creategroupbuy",
            type:"POST",
            data:{
                "groupbuy_name":groupbuy_name,
                "groupbuy_info":groupbuy_info,
                "productid":productid,
                "ownerid":ownerid,
                "groupbuy_count":groupbuy_count,
                "groupbuy_price":groupbuy_price,
                "groupbuy_cost":groupbuy_cost,
                "groupbuy_deliver_add":groupbuy_deliver_add,
                "productFirstImg":firstImg,
                "productSlideimgs":slideImgs,
                "groupbuy_end_time":groupbuy_end_time,
                "groupbuy_start_time":groupbuy_start_time,
                "group_style":group_style,
                "return_cash_rate":return_cash_rate,
                "return_cash_rate_inviter":return_cash_rate_inviter,
                "customer_service":customer_service_select
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("添加团购失败，请尝试输入存在的商品Id！"+data["failedMessage"]);
                }
                else{
                    alert("团购创建成功！");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("团购创建失败！"+JSON.stringify(e));
            }

        })
    })


    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    //获取了产品的ID值
    var productid = getQueryString("productid");

    //从商品页跳转过来先获取商品信息
    $.ajax({
        url: "/ketuan/backmanage/getproductinfo?productid=" + productid,//请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        async: false,//是否异步请求
        success: function (data) {
            if (data["failed"]) {
                /*//console.error(data["failedMessage"]);
                alert("获取商品失败，先执行");
                alert(data["failedMessage"]);*/
                return;
            }
            var product = data["data"]["productdetails"];
            var productName=product["productName"];
            var productId=product["id"];
            var productInfo=product["productInfo"];
            var productCost=product["productCost"];
            var productProduceAddress=product["productProduceAddress"];
            var productProduceCity=product["productProduceCity"];
            var productProduceProvince=product["productProduceProvince"];
            $("#groupbuy_name").val(productName);
            $("#groupbuy_info").val(productInfo);
            $("#groupbuy_cost").val(productCost);
            $("#groupbuy_deliver_add").val(productProduceProvince+productProduceCity);
            $("#product_id").val(productId);
        }
    })

    //获取可用的客服信息
    $.ajax({
        url: "/ketuan/backmanage/getusefullcustomservice",             //请求地址
        dataType: "json",//数据格式
        type: "get",//请求方式
        async: false,//是否异步请求
        success: function (data) {
            if (data["failed"]) {
                alert("获取商品失败，先执行");
                console.error(data["failedMessage"]);
                return;
            }
            else{
                var authorityList = data["data"]["authorityList"];
            }
            authorityList.forEach(function (element) {
                var option = "<option value='"+element["authorityId"]+"'>"+element["authorityName"]+"</option>"
                $("#customer_service_select").append(option);
            })
        }
    })

})
