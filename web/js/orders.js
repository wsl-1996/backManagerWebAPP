$(function(){

    function formatDateTime(timeStamp) {
        var date = new Date();
        date.setTime(timeStamp);
        var y = date.getFullYear();
        var m = date.getMonth() + 1;
        m = m < 10 ? ('0' + m) : m;
        var d = date.getDate();
        d = d < 10 ? ('0' + d) : d;
        var h = date.getHours();
        h = h < 10 ? ('0' + h) : h;
        var minute = date.getMinutes();
        var second = date.getSeconds();
        minute = minute < 10 ? ('0' + minute) : minute;
        second = second < 10 ? ('0' + second) : second;
        return y + '-' + m + '-' + d+' '+h+':'+minute+':'+second;
    };


    $("#refresh_btn").on("click",function (e){
        getAllOrders();
    })

    $("#searchBtn").on("click",function (e){
        var key = $("#searchKey")[0].value;
        if(key==""||key==undefined){
            getAllOrders();
            return
        }
        $.ajax({
            url:"/ketuan/backmanage/searchorders?key="+key,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("搜索订单失败！"+data["failedMessage"]);
                }
                else{
                    var orders = data["data"]["searchResult"];
                        $("#ordersList tbody").html("");
                    for(var i=0;i<orders.length;i++){
                        appendTr(orders[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("搜索订单失败！"+JSON.stringify(e));
            }
        })
    })

    var compare = function (obj1, obj2) {
        var val1 = obj1["orderTime"];
        var val2 = obj2["orderTime"];
        if (val1 < val2) {
            return 1;
        } else if (val1 > val2) {
            return -1;
        } else {
            return 0;
        }
    }

    function getAllOrders() {
        $.ajax({
            url:"/ketuan/backmanage/orderlistall",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("获取订单失败！"+data["failedMessage"]);
                }
                else{
                    var orders = data["data"]["orders"];
                    $("#ordersList tbody").html("");
                    orders.sort(compare);
                    for(var i=0;i<orders.length;i++){
                        appendTr(orders[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取订单失败！"+JSON.stringify(e));
            }
        })
    }

    function appendTr(order){
        var state = "待付款";
        var labelType = "label-default";
        switch (order["state"]){
            case 1:
                state="待付款";
                break;
            case 2:
                state="待发货";
                break;
            case 3:
                state="代收货";
                break;
            case 4:
                state="待评价";
                break;
            case 5:
                state="已完成";
                labelType = "label-primary";
                break;
            case 6:
                state="已取消";
                labelType = "label-danger";
                break;
        }

        var s = '<tr>'
            +'          <td class="project-status">'
            + '             <span class="label '+labelType+'">'+state
            +'          </td>'
            +'          <td class="project-title">'
            +'              <a href="orders_detail.html?orderid='+order["id"]+'">'+order["descript"]
            +'              </a>'
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <small>订单金额：</small>'+order["totalPrice"]
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <small>支付方式：</small>'+order["paymethod"]
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <small>下单时间：</small>'+formatDateTime(order["orderTime"])
            +'          </td>'
            + '         <td class="project-people">'
            +'          </td>'
            // +'          <td class="project-actions">'
            // +'              <a href="orders_detail.html#" class="btn btn-white btn-sm"><i class="fa fa-folder"></i> 查看 </a>'
           // +'              <a href="projects.html#" class="btn btn-white btn-sm"><i class="fa fa-pencil"></i> 下线 </a>'
           //  +'          </td>'
            +'      </tr>'

        $("#ordersList tbody").append(s);
    }

    getAllOrders();

})
