var currentExpressId;
function editExpress(expressId,productId,expressCode,productName,productImg) {
    console.info("修改快递定价信息："+expressId);
    currentExpressId = expressId;
    $('#myModal').modal('show');
    $("#express_list").val(expressCode);
    $("#product_id")[0].innerHTML = productId;
    $("#product_name")[0].innerHTML  = productName;
    $("#product_img").attr("src",productImg);
    $("input").attr("disabled", false);
    $("#express_list").attr("disabled", false);
    $("#saveExpressButton").attr("disabled", false);

    getExpressInfo(expressId);
}

function showExpress(expressId,productId,expressCode,productName,productImg) {
    console.info("查看快递定价信息："+expressId);
    $('#myModal').modal('show');
    $("#express_list").val(expressCode);
    $("#product_id")[0].innerHTML = productId;
    $("#product_name")[0].innerHTML  = productName;
    $("#product_img").attr("src",productImg);
    $("input").attr("disabled", true);
    $("#saveExpressButton").attr("disabled", true);
    getExpressInfo(expressId);
}

function getExpressInfo(expressId){
    $.ajax({
        url:"/ketuan/backmanage/getexpressagedetails?expressageid="+expressId,
        type:"GET",
        dataType: "json",
        success:function (data) {
            console.info(JSON.stringify(data));
            if(data["failed"]){
                alert("获取快递失败！"+data["failedMessage"]);
            }
            else{
                var expressages = data["data"]["expressage"];
                var price_stand_string =expressages["priceStand"];
                var price_stands = JSON.parse(price_stand_string);
                var provincePriceList = $("#price_stand_table")[0].children;
                for(var provinceDiv in provincePriceList){
                    var id = provincePriceList[provinceDiv].id;
                    console.info("id: "+id);
                    if(id==null||id==""){
                        continue;
                    }
                    var provinceName = "default";
                    if(id!="default"){
                        provinceName = ChineseDistricts["86"][id];
                    }
                    for(var price_stand in price_stands){
                        var price = price_stands[price_stand][provinceName];
                        if(price==null||price==""){
                            continue;
                        }
                        $("#price_in_province_"+id).val(price);
                    }
                }
            }
        },
        error:function (e) {
            console.error(JSON.stringify(e));
            alert("获取快递定价信息失败！");
        }
    })
}

$(function(){

    $('#myModal').on('hide.bs.modal',
        function() {
            $("input").attr("disabled", false);
        })

    $("#saveExpressButton").on("click",function (e) {
        var postData={};
        var map = express_map.common;
        var expressCode = $("#express_list").val();
        var express_name = map[expressCode];
        var productId = $("#product_id")[0].innerHTML;
        var provincePriceList = $("#price_stand_table")[0].children;
        var provincePrices = [];
        for(var provinceDiv in provincePriceList){
            var id = provincePriceList[provinceDiv].id;
            console.info("id: "+id);
            if(id==null||id==""){
                continue;
            }
            var provinceName = "default";
            if(id!="default"){
                provinceName = ChineseDistricts["86"][id];
            }
            var priceOfProvince = $("#price_in_province_"+id).val();
            var provincePrice = {};
            provincePrice[provinceName]=priceOfProvince;
            provincePrices.push(provincePrice);
        }
        postData["expressCode"] = expressCode;
        postData["express_name"] = express_name;
        postData["productId"] = productId;
        postData["price_stand"] = JSON.stringify(provincePrices);
        postData["expressId"] = currentExpressId;
        $.ajax({
            url:"/ketuan/backmanage/savenewexpress",
            type:"POST",
            data:postData,
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("保存新的快递信息失败！"+data["failedMessage"]);
                }
                else{
                    alert("保存成功！");
                    getAllExpressages();
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("保存新的快递信息失败！"+JSON.stringify(e));
            }
        })
    })

    $("#loading-example-btn").on("click",function (e){
        getAllExpressages();
    })

    function generateExpressList() {
        var map = express_map.common;
        for (var key in map) {
            var s="<option value=\""+key+"\">"+map[key]+"</option>";
            $("#express_list").append(s);
            console.log(key+"  "+map[key]);
        }
    }
    generateExpressList();
    function generateProvincePriceList(){
        var chineseProvince = ChineseDistricts["86"];
        $("#price_stand_table").html("");
        var s='<div class="row" id="default"> ' +
            '           <label class="col-sm-5">**全国默认价格【没有填写价格的省份使用默认价格发货】：</label>' +
            '                       <div class="col-sm-3">' +
            '                           <div class="input-group m-b"><span class="input-group-addon">&yen;</span>\n' +
            '                               <input type="text" class="form-control province_price_input" id="price_in_province_default" />\n' +
            '                           </div>' +
            '           </div>'+
            '   </div>';
        $("#price_stand_table").append(s);
        for(var key in chineseProvince){
            var s='<div class="row" id="'+key+'"> ' +
                '           <label class="col-sm-2 col-sm-offset-2">'+chineseProvince[key]+'</label>' +
                '                       <div class="col-sm-3">' +
                '                           <div class="input-group m-b"><span class="input-group-addon">&yen;</span>\n' +
                '                               <input type="text" class="form-control province_price_input" id="price_in_province_'+key+'" />\n' +
                '                           </div>' +
                '           </div>'+
                '   </div>' +
                '<div class="hr-line-dashed" style="margin: 0px"></div>';
            $("#price_stand_table").append(s);
        }
    }
    generateProvincePriceList();

    $("#price_in_province_default").on("blur",function () {
        var defaultPrice = $("#price_in_province_default").val();
        setDefaultPrice(defaultPrice);
    })

    function setDefaultPrice(defaultPrice) {
        $(".province_price_input").val(defaultPrice);
    }

    $("#searchBtn").on("click",function (e){
        var key = $("#searchKey")[0].value;
        if(key==""||key==undefined){
            getAllExpressages();
            return
        }
        $.ajax({
            url:"/ketuan/backmanage/expressagesearch?key="+key,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("搜索快递失败！"+data["failedMessage"]);
                }
                else{
                    var expressages = data["data"]["expressages"];
                    $("#expressagesList tbody").html("");
                    for(var i=0;i<expressages.length;i++){
                        appendTr(expressages[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("搜索快递失败！"+JSON.stringify(e));
            }
        })
    })

    function getAllExpressages() {
        $.ajax({
            url:"/ketuan/backmanage/expressagelistall?isnew=1",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("获取快递失败！"+data["failedMessage"]);
                }
                else{
                    var expressages = data["data"]["expressages"];
                        $("#expressagesList tbody").html("");
                    for(var i=0;i<expressages.length;i++){
                        appendTr(expressages[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取快递失败！"+JSON.stringify(e));
            }
        })
    }

    function appendTr(expressage){
        var state = "商品未上线";
        var labelType = "label-default";
        switch (expressage["productState"]) {
            case 1:
                state = "商品已上线";
                labelType = "label-primary";
                break;
            case 2:
                state = "商品已下线";
                labelType = "label-danger";
                break;
        }
        var createTime = formatDateTime(expressage["expressCreateTime"]);
        var s = '<tr id="'+expressage["expressId"]+'" >'
            +'          <td class="project-status">'
            +'              <span class="label '+labelType+'">'+state
            +'          </td>'
            +'          <td class="project-status">'
            +'              <img style="width: 30px;height: 30px" src="'+expressage["productFirstImg"]+'" onclick="showExpress(\''+expressage["expressId"]+'\',\''+expressage["productId"]+'\',\''+expressage["expressCode"]+'\',\''+expressage["productName"]+'\',\''+expressage["productFirstImg"]+'\')">'
            +'          </td>'
            +'          <td class="project-status" onclick="showExpress(\''+expressage["expressId"]+'\',\''+expressage["productId"]+'\',\''+expressage["expressCode"]+'\',\''+expressage["productName"]+'\',\''+expressage["productFirstImg"]+'\')">'
            +'              <span>'+expressage["productName"]+'</span>'
            +'          </td>'
            +'          <td class="project-title">'
            +'              <span>'+expressage["expressName"]+'</span>'
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <span>'+createTime+'</span>'
            +'          </td>'
            +'          <td class="project-people">'
            +'          </td>'
            +'          <td class="project-actions">'
            +'           <button class="btn btn-white btn-sm change" id="change" onclick="editExpress(\''+expressage["expressId"]+'\',\''+expressage["productId"]+'\',\''+expressage["expressCode"]+'\',\''+expressage["productName"]+'\',\''+expressage["productFirstImg"]+'\')">'
            +'             <i class="fa fa-folder"></i>编辑</button>'
            +'          </td>'
            +'      </tr>';
        $("#expressagesList tbody").append(s);
    }
    getAllExpressages();

})


