$(function(){

    var map = express_map.common;
    for (var key in map) {
        var s="<option value=\""+key+"\">"+map[key]+"</option>";
        $("#express_list").append(s);
        console.log(key+"  "+map[key]);
    }
    


    var chineseProvince = ChineseDistricts["86"];
    $("#price_stand_table").html("");
    var s='<div class="row" id="default"> ' +
        '           <label class="col-sm-5 col-sm-offset-2">**全国默认价格【没有填写价格的省份使用默认价格发货】：</label>' +
        '                       <div class="col-sm-3">' +
        '                           <div class="input-group m-b"><span class="input-group-addon">&yen;</span>\n' +
        '                               <input type="text" class="form-control province_price_input" id="price_in_province_default" />\n' +
        '                           </div>' +
        '           </div>'+
        '   </div>';
    $("#price_stand_table").append(s);
    $("#price_in_province_default").on("blur",function () {
        var defaultPrice = $("#price_in_province_default").val();
        setDefaultPrice(defaultPrice);
    })
    
    function setDefaultPrice(defaultPrice) {
        $(".province_price_input").val(defaultPrice);
    }

    for(var key in chineseProvince){
        var s='<div class="row" id="'+key+'"> ' +
            '           <label class="col-sm-2">'+chineseProvince[key]+'</label>' +
'                       <div class="col-sm-3">' +
'                           <div class="input-group m-b"><span class="input-group-addon">&yen;</span>\n' +
'                               <input type="text" class="form-control province_price_input" id="price_in_province_'+key+'" />\n' +
'                           </div>' +
            '           </div>'+
            '   </div>' +
            '<div class="hr-line-dashed" style="margin: 0px"></div>';
        $("#price_stand_table").append(s);
    }

    function getAllClassifyCode() {
        $.ajax({
            url:"/ketuan/backmanage/getproductclassify",
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("获取所有分类失败！" + data["failedMessage"]);
                }
                else {
                    var classifyCode = data["data"]["productclassify"];
                    for(var i=0; i<classifyCode.length;i++){
                        appendClassifyCode(classifyCode[i]);
                    }
                    $("#classify_code_list tbody tr").on("click", getClassifyCode);
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("获取所有分类失败！" + JSON.stringify(e));
            }
        })
    }
    function appendClassifyCode(classifyCode){
        var code = classifyCode["code"];
        var name = classifyCode["name"];
        var g1 = code.substr(0,2);
        var g2= code.substr(2,2);
        var g3= code.substr(4,2);
        var classify_row ="";
        if(g2=="00" && g3=="00"){
            classify_row='<tr id="'+code+'"> <td>'+name+'</td><td></td><td></td></tr>';
        }
        else if(g3=="00"){
            classify_row='<tr id="'+code+'"> <td></td><td>'+name+'</td><td></td></tr>';
        }
        else{
            classify_row='<tr id="'+code+'"> <td></td><td></td><td>'+name+'</td></tr>';
        }
        $("#classify_code_list tbody").append(classify_row);
    }

    function getClassifyCode(data) {
        var currentId = $(this).attr("id");
        $("#product_classifyCode").val(currentId);
        //$("#product_create_search").hide();
    }

    getAllClassifyCode();

    function getAllHotLabels() {
        $.ajax({
            url:"/ketuan/backmanage/hostlabels",
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("获取所有热门标签失败！" + data["failedMessage"]);
                }
                else {
                    var labels = data["data"]["labels"];
                    for(var i=0; i<labels.length;i++){
                        appendHotLabels(labels[i]);
                    }
                    $(".hotlabel").on("click", addLabel);
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("获取所有热门标签失败！" + JSON.stringify(e));
            }
        })
    }
    
    function addLabel(data) {
        var labelId = $(this).attr("id");
        var labelName=$(this).find("#"+labelId).context.innerHTML;
        var label_before1=$("#product_lable").val();
        $("#product_lable").val(label_before1+" "+labelName);
    }

    function appendHotLabels(label) {
        var labelBtn = '<button type="button" class="label label-primary hotlabel" id="'+label["id"]+'">'+label["name"]+'</button>';
        $("#label_select_div").append(labelBtn);
    }

    getAllHotLabels();
    
    var val=$("#product_lable").val();
    $("#label_1").on("click",function (e) {
        var label_before1=$("#product_lable").val();//先获取之前输入的值
        var label_1=$("#label_1")[0].innerHTML;
        $("#product_lable").val(label_before1+" "+label_1);//点击标签一，input出现包邮
    })
    $("#label_2").on("click",function (e) {
        var label_before2=$("#product_lable").val();
        var  label_2=$("#label_2")[0].innerHTML;
        $("#product_lable").val(label_before2+" "+label_2);
    })
    $("#label_3").on("click",function (e) {
        var label_before3=$("#product_lable").val();
        var label_3=$("#label_3")[0].innerHTML;
        $("#product_lable").val(label_before3+" "+label_3);
    })
    $("#label_4").on("click",function (e) {
        var label_before4=$("#product_lable").val();
        var label_4=$("#label_4")[0].innerHTML;
        $("#product_lable").val(label_before4+" "+label_4);
    })

//商品产地地址的获取
    $('#distpicker').on("change",function (data) {
        var address_before1=$("#productproduceadd").val();
        var province1= $('#province option:selected').val();
        var city1= $('#city option:selected').val();
        var district1= $('#district option:selected').val();
        $("#productproduceadd").val(address_before1+province1+city1+district1);

        //var address_before2=$("#productproduceadd").val();
       //var nothing1= $('#productproduceadd').value;
        var province2= $('#province option:selected').val();
        var city2= $('#city option:selected').val();
        var district2= $('#district option:selected').val();
        $("#productproduceadd").val(province2+city2+district2);

        //var address_before3=$("#productproduceadd").val();
        var province3= $('#province option:selected').val();
        var city3= $('#city option:selected').val();
        var district3= $('#district option:selected').val();
        $("#productproduceadd").val(province3+city3+district3);

    })

    //发货地址的获取值
   $('#distpicker2').on("change",function (data) {
       var address_before1=$("#productshipment").val();    //当我选中省级时，input显示三级下拉值
       var province1= $('#province2 option:selected').val();
       var city1= $('#city2 option:selected').val();
       var district1= $('#district2 option:selected').val();
       $("#productshipment").val(address_before1+province1+city1+district1);

       var province2= $('#province2 option:selected').val();
       var city2= $('#city2 option:selected').val();
       var district2= $('#district2 option:selected').val();
       $("#productshipment").val(province2+city2+district2);

       //var address_before3=$("#productproduceadd").val();
       var province3= $('#province2 option:selected').val();
       var city3= $('#city2 option:selected').val();
       var district3= $('#district2 option:selected').val();
       $("#productshipment").val(province3+city3+district3);
    })

    //供货人的选择和获取
    $("#ownerid").on("click",function (e) {
        $("#product_create_search").show();
      //该input标签的记忆功能取消或者不取消
        $("#ownerid").attr('placeholder',null);
    })

    $("#product_classifyCode").on("click",function (e) {
        $("#classify_code_modal").modal("show");
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

    $("#createBtn").on("click",function(e){

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
        $("#product_create_search").hide();
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

        // for (var i=0;i<$("#product_createList tbody tr").length;i++) {
        //
        // }
        $("#searchList tbody").append(s);
            $("#searchList tbody tr").on("click", getid);

    }

    $("#input_productCreate input").on("click",function () {
        var name_choose=$('input:radio[name="a"]:checked').val();
        if(name_choose==1) {
            //选择个人;
            getAllUsers();
        }
        if(name_choose==0) {
            //radio选择商家
            getAllMerchants();
        }
    })

    function trim(str){
        var newStr = str.replace(/^\s*$/g,'');
        return newStr;
    }

    $("#createBtn").on("click",function (e) {
        console.info("创建产品");


         //商品供货量的约束
        var productcount1=$("#productcount").val();
        if(productcount1==null){
           alert("请输入商品供货量信息！");
        }else if (!(/(^[0-9]*[1-9][0-9]*$)/.test(productcount1))){
            alert("商品供货量输入的不是正整数");return false;
        }


        //零售价和成本价的约束
        var productprice1=$("#productprice").val();
        if(productprice1==null){
            alert("请输入商品零售价信息！");
        }else if (!(/(^[0-9]*[1-9][0-9]*$)/.test(productprice1))){
            alert("商品的零售价输入的不是正整数");return false;
        }
        var product_cost1=$("#product_cost").val();
        if(product_cost1==null){
            alert("请输入商品成本价信息！");
        }else if (!(/(^[0-9]*[1-9][0-9]*$)/.test(product_cost1))){
            alert("商品的成本价输入的不是正整数");return false;
        }


        var productname = $("#product_name")[0].value;
        var productinfo = $("#product_info")[0].value;
        var productcode = $("#product_classifyCode")[0].value;
        var productlable = $("#product_lable")[0].value;

        var labels = trim(productlable);
        var labelarr=labels.split(" ")
        var selectedLlabels = JSON.stringify( labelarr );

        var type_choose=$('input:radio[name="a"]:checked').val();
        //var productownertype = $()
        var productownerid = $("#ownerid")[0].value;
        var productcount = $("#productcount")[0].value;
        var productprice = $("#productprice")[0].value;
        var productcost = $("#product_cost")[0].value;
        var productproduceadd = $("#productproduceadd")[0].value;
        var productIntroduction = $("#productIntroduction")[0].value;

        var packInfo = $("#package_info")[0].value;
        var after_sale_info = $("#after_sale_info")[0].value;
        var prise_instration = $("#prise_instration")[0].value;
        var productaftersale = {"包装清单":packInfo,"售后服务":after_sale_info,"价格说明":prise_instration};

        var productpackstand={} ;

        $("#parameter_list tr").each(function(){
            var key=$(this).find(".key_input")[0].value;
            var value=$(this).find(".value_input")[0].value;
            productpackstand[key]=value;
        });

        var startTime = $("#datetimepicker_start").val();
        var endTime = $("#datetimepicker_end").val();

        var express_code = $("#express_list").val();
        var express_name = map[express_code];
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

        $.ajax({
            url:"/ketuan/backmanage/createproduct",
            type:"POST",
            data:{
                "productname":productname,
                "productinfo":productinfo,
                "productcode":productcode,
                "productlable":selectedLlabels,
                "productownertype":type_choose,
                "productownerid":productownerid,
                "productcount":productcount,
                "productprice":productprice,
                "productcost":productcost,
                "productproduceadd":productproduceadd,
                "productpackstand":JSON.stringify(productpackstand),
                "productaftersale":JSON.stringify(productaftersale),
                "productIntroduction":productIntroduction,
                "productFirstImg":firstImg,
                "productSlideimgs":slideImgs,
                "productContentimgs":contentImgs,
                "offlinetime":endTime,
                "onlinetime":startTime,
                "express_name":express_name,
                "express_code":express_code,
                "express_price_stand":JSON.stringify(provincePrices)
            },
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("添加商品失败！"+data["failedMessage"]);
                }
                else{
                    alert("商品添加成功！");
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("商家添加失败！"+JSON.stringify(e));
            }
        })
    })

})
