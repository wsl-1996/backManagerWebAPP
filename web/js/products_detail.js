$(document).ready(function() {
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    var productid = getQueryString("productid");
    function getProductInfo(){
        $.ajax({
            url : "/ketuan/backmanage/getproductinfo?productid="+productid,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            async : false,//是否异步请求
            success : function(data) {
                if(data["failed"]){
                    console.error(data["failedMessage"]);
                    alert(data["failedMessage"]);
                    return;
                }
                var product = data["data"]["productdetails"];
                $("#product_name").html(product["productName"]);
                $("#product_id").html(product["id"]);
                $("#product_price").append("￥"+product["price"]);
                $("#product_cost").append("￥"+product["productCost"]);
                $("#product_info").append(product["productInfo"]);

                $("#product_onlinetime").append(product["onlineTime"]);
                if(product["productState"]==0){
                    $("#product_state").html("未上线");
                    $("#product_state").addClass("label-warning");
                }
                else if(product["productState"]==1){
                    $("#product_state").html("已上线");
                    $("#product_state").addClass("label-primary");
                }
                else if(product["productState"]==2){
                    $("#product_state").html("已下线");
                    $("#product_state").addClass("label-warning");
                }
                $("#product_sale_Volume_History").append(product["saleVolumeHistory"]);
                $("#product_sale_Volume_Monthly").append(product["saleVolumeMonthly"]);
                $("#product_onlineTime").append(product["onlineTime"]);
                $("#product_offlineTime").append(product["offlineTime"]);
                $("#product_parameter").append(product["packStand"]);
                $("#product_afterSale").append(product["afterSale"]);
                $("#product_label").append(product["productLabel"]);

                $("#product_ownerType").html(product["ownerType"]);
                $("#product_userId").html(product["userId"]);
                $("#product_merchantId").html(product["merchantId"]);

                $("#product_imagesAddress").html(product["imagesAddress"]);

                $("#product_starLevel").html(product["starLevel"]);

                $("#product_evaluateLabel").html(product["evaluateLabel"]);

                $("#product_first_img").append('<img style="width: 200px;" src="'+product["productFistImg"]+'">');

                var slideImg = product["productSlideImg"];
                var slideImgs = JSON.parse(slideImg);
                for(var i=0;i<slideImgs.length;i++){
                    $("#product_slide_imgs").append('<img style="width: 200px;" src="'+slideImgs[i]+'">');
                }

                var contentImg = product["imagesAddress"];
                var contentImgs = JSON.parse(contentImg);
                for(var i=0;i<contentImgs.length;i++){
                    $("#product_detail_imgs").append('<img style="width: 200px;" src="'+contentImgs[i]+'">');
                }
                $("#product_productProduceAddress").html(product["productProduceAddress"]);
            },
            error:function (e) {
                console.error(e.toString());
            }
        })
    }

    $("#online_btn").on("click",function (data) {
        $.ajax(
            {
                url: "/ketuan/backmanage/changeproductstate",
                data: {
                    productState: 1,
                    productId:productid
                },
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if(data["failed"]){
                        alert("商品上线失败");
                    }
                    else{
                        alert("商品已成功上线");
                    }
                },
                error:function (e) {
                    console.error(JSON.stringify(e));
                    alert("商品上线失败！"+JSON.stringify(e));
                }
            }
        )
    })

    $("#offline_btn").on("click",function (data) {
        $.ajax(
            {
                url: "/ketuan/backmanage/changeproductstate",
                data: {
                    productState: 2,
                    productId:productid
                },
                type: "GET",
                dataType: "json",
                success: function (data) {
                    if(data["failed"]){
                        alert("商品下线失败");
                    }
                    else{
                        alert("商品已成功下线");
                    }
                },
                error:function (e) {
                    console.error(JSON.stringify(e));
                    alert("商品下线失败！"+JSON.stringify(e));
                }
            }
        )
    })

    getProductInfo();
    $("#establishGroup").attr('href','groupbuy_create.html?productId='+productid);

    $("#refresh_product_btn").on("click",function (e) {
       //getProductInfo();
    })

    $("#edit_product_price").on("click",function (data) {
        $("#product_price_input").show();
        $("#product_price_input").val("");
        $("#edit_product_price_undo").show();
        $("#save_product_price").show();
        $("#product_price").hide();
        $("#edit_product_price").hide();

    })
    $("#edit_product_price_undo").on("click",function (data) {
        $("#product_price_input").hide();
        $("#edit_product_price_undo").hide();
        $("#save_product_price").hide();
        $("#product_price").show();
        $("#edit_product_price").show();
    })
    $("#save_product_price").on("click",function (data) {
        var newPrice = $("#product_price_input").val();
        if(isNaN( newPrice )&&newPrice.value!=0){
            alert("请输入数字");
            return;
        }
        $.ajax({
            url: "/ketuan/backmanage/changeproductprice",
            data: {
                productPrice: newPrice,
                productId:productid
            },
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data["failed"]){
                    alert("商品价格修改失败");
                }
                else{
                    alert("商品价格修改成功");
                    $("#product_price_input").val(newPrice);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商品价格修改失败！"+JSON.stringify(e));
            }
        })
    })

    $("#edit_product_cost").on("click",function (data) {
        $("#product_cost_input").show();
        $("#product_cost_input").val("");
        $("#edit_product_cost_undo").show();
        $("#save_product_cost").show();
        $("#product_cost").hide();
        $("#edit_product_cost").hide();

    })
    $("#edit_product_cost_undo").on("click",function (data) {
        $("#product_cost_input").hide();
        $("#edit_product_cost_undo").hide();
        $("#save_product_cost").hide();
        $("#product_cost").show();
        $("#edit_product_cost").show();
    })
    $("#save_product_cost").on("click",function (data) {
        var newcost = $("#product_cost_input").val();
        if(isNaN( newcost )&&newcost.value!=0){
            alert("请输入数字");
            return;
        }
        $.ajax({
            url: "/ketuan/backmanage/changeproductcost",
            data: {
                productCost: newcost,
                productId:productid
            },
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data["failed"]){
                    alert("商品成本价格修改失败");
                }
                else{
                    alert("商品成本价格修改成功");
                    $("#product_cost_input").val(newcost);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商品成本价格修改失败！"+JSON.stringify(e));
            }
        })
    })

    $("#edit_product_info").on("click",function (data) {
        $("#product_info_input").show();
        $("#product_info_input").val("");
        $("#edit_product_info_undo").show();
        $("#save_product_info").show();
        $("#product_info").hide();
        $("#edit_product_info").hide();

    })
    $("#edit_product_info_undo").on("click",function (data) {
        $("#product_info_input").hide();
        $("#edit_product_info_undo").hide();
        $("#save_product_info").hide();
        $("#product_info").show();
        $("#edit_product_info").show();
    })
    $("#save_product_info").on("click",function (data) {
        var info = $("#product_info_input").val();
        $.ajax({
            url: "/ketuan/backmanage/changeproductinfo",
            data: {
                productInfo: info,
                productId:productid
            },
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data["failed"]){
                    alert("商品描述修改失败");
                }
                else{
                    alert("商品描述修改成功");
                    $("#product_info_input").val(info);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商品描述修改失败！"+JSON.stringify(e));
            }
        })
    })

    $("#edit_product_onlineTime").on("click",function (data) {
        $("#product_onlineTime_input").show();
        $("#product_onlineTime_input").val("");
        $("#edit_product_onlineTime_undo").show();
        $("#save_product_onlineTime").show();
        $("#product_onlineTime").hide();
        $("#edit_product_onlineTime").hide();
        $('#product_onlineTime_input').datetimepicker();
    })
    $("#save_product_onlineTime").on("click",function (data) {
        var new_onlineTime =  $("#product_onlineTime_input").val();
        $.ajax({
            url: "/ketuan/backmanage/changeproductonlinetime",
            data: {
                productOnlineTime: new_onlineTime,
                productId:productid
            },
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data["failed"]){
                    alert("商品上线时间修改失败");
                }
                else{
                    alert("商品上线时间修改成功");
                    $("#product_onlineTime_input").val(new_onlineTime);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商品上线时间修改失败！"+JSON.stringify(e));
            }
        })
    })
    $("#edit_product_onlineTime_undo").on("click",function (data) {
        $("#product_onlineTime_input").hide();
        $("#edit_product_onlineTime_undo").hide();
        $("#save_product_onlineTime").hide();
        $("#product_onlineTime").show();
        $("#edit_product_onlineTime").show();
    })

    $("#edit_product_offlineTime").on("click",function (data) {
        $("#product_offlineTime_input").show();
        $("#product_offlineTime_input").val("");
        $("#edit_product_offlineTime_undo").show();
        $("#save_product_offlineTime").show();
        $("#product_offlineTime").hide();
        $("#edit_product_offlineTime").hide();
        $('#product_offlineTime_input').datetimepicker();
    })
    $("#save_product_offlineTime").on("click",function (data) {
        var new_offlineTime =  $("#product_offlineTime_input").val();
        $.ajax({
            url: "/ketuan/backmanage/changeproductofflinetime",
            data: {
                productOfflineTime: new_offlineTime,
                productId:productid
            },
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data["failed"]){
                    alert("商品上线时间修改失败");
                }
                else{
                    alert("商品上线时间修改成功");
                    $("#product_offlineTime_input").val(new_onlineTime);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商品上线时间修改失败！"+JSON.stringify(e));
            }
        })
    })
    $("#edit_product_offlineTime_undo").on("click",function (data) {
        $("#product_offlineTime_input").hide();
        $("#edit_product_offlineTime_undo").hide();
        $("#save_product_offlineTime").hide();
        $("#product_offlineTime").show();
        $("#edit_product_offlineTime").show();
    })


    $("#edit_product_label").on("click",function (data) {

    })
    $("#edit_product_label").on("click",function (data) {
        $("#product_label_input").show();
        $("#product_label_input").val("");
        $("#edit_product_label_undo").show();
        $("#save_product_label").show();
        $("#product_label").hide();
        $("#edit_product_label").hide();

    })
    $("#edit_product_label_undo").on("click",function (data) {
        $("#product_label_input").hide();
        $("#edit_product_label_undo").hide();
        $("#save_product_label").hide();
        $("#product_label").show();
        $("#edit_product_label").show();
    })
    $("#save_product_label").on("click",function (data) {
        var newLabel = $("#product_label_input").val();
        var newLabels = newLabel.split(" ");
        var labels=[];
        for(var label in newLabels){
            if(newLabels[label]!="" || newLabels[label]!=" "){
                labels.push(newLabels[label]);
            }
        }
        $.ajax({
            url: "/ketuan/backmanage/changeproductlabel",
            data: {
                productLabels: JSON.stringify(labels),
                productId:productid
            },
            type: "GET",
            dataType: "json",
            success: function (data) {
                if(data["failed"]){
                    alert("商品标签修改失败");
                }
                else{
                    alert("商品标签修改成功");
                    $("#product_label_input").val(newPrice);
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商品标签修改失败！"+JSON.stringify(e));
            }
        })
    })


})