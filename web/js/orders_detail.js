$(document).ready(function() {
    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }
    var orderid = getQueryString("orderid");

    $("#refresh_btn").on("click",function () {
        getDetail();
    })

    function getShippingAddress(addrId){
        $.ajax({
            url : "/ketuan/backmanage/getaddinfo?addrid="+addrId,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            async : false,//是否异步请求
            success : function(data) {   //如何发送成功
                if(data["failed"]){
                    alert("获取配送地址失败");
                    console.error(data["failedMessage"]);
                }
                else{
                    var addr = data["data"]["address"];
                    console.info(addr);
                    var addrDetail = addr["addressDetail"];
                    var province = addr["province"];
                    var city = addr["city"];
                    var districts = addr["districts"];
                    var addString = province+" "+city+" "+districts+" "+addrDetail;
                    $("#deliver_add").html(addString);
                }
            },
            error:function (e) {
                alert("获取配送地址失败");
                console.error(e.toString());
            }
        })

    }

    function getTrackDetail(trackId){
        $.ajax({
            url : "/ketuan/backmanage/gettrackdetail?trackid="+trackId,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            async : false,//是否异步请求
            success : function(data) {   //如何发送成功
                if(data["failed"]){
                    alert("获取快递详情失败");
                    console.error(data["failedMessage"]);
                }
                else{
                    var track = data["data"]["track"];
                    console.info(track);
                    if(track==null){
                        return;
                    }
                    var trackNumber = track["trackNumber"];
                    var trackCode = track["trackCode"];
                    var trace = track["track"];

                    var tarckName = map[trackCode];
                    $("#express_company").html(tarckName);
                    $("#express_id").html(trackNumber);

                    var traceList = JSON.parse(trace);
                    console.info(traceList);
                    $("#expressDetails tbody").html("");
                    for(var i=0;i<traceList.length;i++){
                        var accept = traceList[i]["AcceptStation"];
                        var time = traceList[i]["AcceptTime"];
                        var s = '<tr>'
                            +'          <td class="project-status">'
                            +               time
                            +'          </td>'
                            +'          <td class="project-title">'
                            +               accept
                            +'          </td>'
                            +'      </tr>'
                        $("#expressDetails tbody").append(s);
                    }
                }
            },
            error:function (e) {
                alert("获取快递详情失败");
                console.error(e.toString());
            }
        })
    }

    $("#confirm_send_goods").on("click",function () {
        var express_code = $("#express_list").val();
        var express_name = map[express_code];
        var track_id = $("#order_id_input").val();
        if(track_id==null||track_id==""){
            alert("请填写快递单号");
            return;
        }
        $.ajax({
            url : "/ketuan/backmanage/sendgood?orderid="+orderid+"&tarckid="+track_id+"&express_code="+express_code+"&express_name="+express_name,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            async : false,//是否异步请求
            success : function(data) {   //如何发送成功
                if(data["failed"]){
                    alert("发货失败");
                    console.error(data["failedMessage"]);
                }
                else{
                    alert("发货成功");
                }
            },
            error:function (e) {
                alert("发货失败");
                console.error(e.toString());
            }
        })

    })

    var map = express_map.common;
    for (var key in map) {
        var s="<option value=\""+key+"\">"+map[key]+"</option>";
        $("#express_list").append(s);
        console.log(key+"  "+map[key]);
    }

    function getDetail(){
        $.ajax({
            url : "/ketuan/backmanage/getorderdetails?orderid="+orderid,//请求地址
            dataType : "json",//数据格式
            type : "get",//请求方式
            async : false,//是否异步请求
            success : function(data) {   //如何发送成功
                var order= data["data"]["orderDetails"];
                console.info(order);
                $("#order_sendName").html(order["sendName"]);
                $("#order_state").html(order["orderState"]);
                $("#order_productTitle").html(order["productTitle"]);
                $("#order_descript").html(order["typeSpecification"]);
                $("#deliver_add").html(order["sendAddress"]);
                $("#order_sendTel").html(order["sendTel"]);
                $("#order_productPrice").html(order["productPrice"]);
                $("#order_acticePrice").html(order["acticePrice"]);
                $("#balance_deduction").html(order["deduction"]);
                $("#pay_price_real").html(order["totalPrice"]);
                $("#carriagePrice").html(order["carriagePrice"]);
                $("#order_time").html(order["orderTime"]);
                $("#order_payTime").html(order["payTime"]);
                $("#deliverTime").html(order["deliverTime"]);
                $("#express_company").html(order["trackCode"]);
                $("#express_id").html(order["trackId"]);
                $("#order_meno").html(order["meno"]);
                $("#order_paymethod").html(order["paymethod"]);
                $("#order_id").html(order["id"]);
                $("#order_receiptTime").html(order["receiptTime"]);
                $("#order_sums").html(order["sums"]);
                $("#pay_price_real").html(order["sumPrice"]);
                if(order["state"]=="待付款"||order["state"]=="待发货"){
                    console.info("未发货");
                }
                getShippingAddress(order["sendAddress"]);
                getTrackDetail(order["trackId"]);

            },
            error:function (e) {
                console.error(e.toString());
            }
        })
    }

    getDetail();
})