function offlineProduct(productId){
    $.ajax(
        {
            url: "/ketuan/backmanage/changeproductstate",
            data: {
                productState: 2,
                productId:productId
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
}

function onlineProduct(productId){
    $.ajax(
        {
            url: "/ketuan/backmanage/changeproductstate",
            data: {
                productState: 1,
                productId:productId
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
}

$(function(){

    $("#loading-example-btn").on("click",function (e){
        getAllProducts();
    })

    $("#searchBtn").on("click",function (e){
        var key = $("#searchKey")[0].value;
        if(key==""||key==undefined){
            getAllProducts();
            return
        }
        $.ajax({
            url:"/ketuan/backmanage/productsearch?key="+key,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("搜索商品失败！"+data["failedMessage"]);
                }
                else{
                    var products = data["data"]["products"];
                        $("#productsList tbody").html("");
                    for(var i=0;i<products.length;i++){
                        appendTr(products[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("搜索团购失败！"+JSON.stringify(e));
            }
        })
    })

    function getAllProducts() {
        $.ajax({
            url:"/ketuan/backmanage/productlistall",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("获取商品失败！"+data["failedMessage"]);
                }
                else{
                    var products = data["data"]["products"];
                        $("#productsList tbody").html("");
                    for(var i=0;i<products.length;i++){
                        appendTr(products[i]);
                    }
                    setA_btn();
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取商品失败！"+JSON.stringify(e));
            }
        })
    }

    function appendTr(product){
        var state = "已上线";
        var operateBtn = '<button id="product_offline_btn" onclick="offlineProduct(\''+product["id"]+'\')" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> 下线 </button>'
        var labelType="label-primary";
        switch (product["productState"]){
            case 0:
                state="未上线";
                labelType="laebl-warning";
                operateBtn = '<button id="product_offline_btn" onclick="onlineProduct(\''+product["id"]+'\')" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> 上线 </button>'
                break;
            case 1:
                state="已上线";
                break;
            case 2:
                state="已下线";
                labelType="laebl-warning";
                operateBtn = '<button id="product_offline_btn" onclick="onlineProduct(\''+product["id"]+'\')" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> 上线 </button>'
                break;
        }
        var s = '<tr id="'+product["id"]+'">'
            +'          <td class="project-status">'
            + '             <span class="label '+labelType+'">'+state+'</span>'
            +'          </td>'
            +'          <td class="project-title">'
            +'              <a class="a_btn" h_id="products_detail.html?productid='+product["id"]+'">'+product["productName"]
            +'              </a>'
            +'              <br/>'
            +'              <small>上线时间：'+product["onlineTime"]+'</small>'
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <small>历史销量:</small>'+product["saleVolumeHistory"]
            +'              <br/>'
            +'              <small>本月销量:</small>'+product["saleVolumeMonthly"]
            +'          </td>'
            + '         <td class="project-people">'
            +'          </td>'
            +'          <td class="project-actions">'
            + operateBtn
            +'          </td>'
            +'      </tr>'
        $("#productsList tbody").append(s);

    }
    getAllProducts();
    function setA_btn(){
        $('.a_btn').unbind('click').on('click', function () {
            var detailurl=$(this)[0].getAttribute('h_id')
            console.log('detailurl',detailurl)
            page(detailurl,'商品详情')
        })
    }


    function page(url, title) {
        var nav = $(window.parent.document).find('.J_menuTabs .page-tabs-content ');
        $(window.parent.document).find('.J_menuTabs .page-tabs-content ').find(".J_menuTab.active").removeClass("active");
        $(window.parent.document).find('.J_mainContent').find("iframe").css("display", "none");
        var iframe = '<iframe class="J_iframe" name="iframe101" width="100%" height="100%" src="' + url + '" frameborder="0" data-id="' + url
            + '" seamless="" style="display: inline;"></iframe>';
        $(window.parent.document).find('.J_menuTabs .page-tabs-content ').append(
            ' <a href="javascript:;" class="J_menuTab active" data-id="' + url + '">' + title + ' <i class="fa fa-times-circle"></i></a>');
        $(window.parent.document).find('.J_mainContent').append(iframe);
    }
})
