$(function(){

    $("#loading-example-btn").on("click",function (e){
        getAllMerchants();
    })

    $("#searchBtn").on("click",function (e){
        var key = $("#searchKey")[0].value;
        if(key==""||key==undefined){
            getAllMerchants();
            return
        }
        $.ajax({
            url:"/ketuan/backmanage/merchantsearch?key="+key,
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("搜索商家失败！"+data["failedMessage"]);
                }
                else{
                    var merchants = data["data"]["merchants"];
                        $("#merchantsList tbody").html("");
                    for(var i=0;i<merchants.length;i++){
                        appendTr(merchants[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("搜索商家失败！"+JSON.stringify(e));
            }
        })
    })

    function getAllMerchants() {
        $.ajax({
            url:"/ketuan/backmanage/merchantlistall",
            type:"GET",
            dataType: "json",
            success:function (data) {
                console.info(JSON.stringify(data));
                if(data["failed"]){
                    alert("获取商家失败！"+data["failedMessage"]);
                }
                else{
                    var merchants = data["data"]["merchants"];
                    $("#merchantsList tbody").html("");
                    for(var i=0;i<merchants.length;i++){
                        appendTr(merchants[i]);
                    }
                }
            },
            error:function (e) {
                console.error(JSON.stringify(e));
                alert("获取商家失败！"+JSON.stringify(e));
            }
        })
    }

    function appendTr(merchant){
        var s = '<tr id="'+merchant["id"]+'">'
            +'          <td class="project-status">'
            + '             <span class="label label-primary">@'
            +'          </td>'
            +'          <td class="project-title">'
            +'              <a href="merchant_detail.html?merchantid='+merchant["id"]+'">'+merchant["name"]
            +'              </a>'
            +'          </td>'
            +'          <td class="project-completion">'
            +'              <small>联系电话：</small>'+merchant["phone"]
            +'          </td>'
            + '         <td class="project-people">'
            +'          </td>'
            +'          <td class="project-actions">'
            +'              <a href="projects.html#" class="btn btn-white btn-sm"><i class="fa fa-folder"></i> 查看 </a>'
            +'              <button class="btn btn-white btn-sm" id="delete_merchant"><i class="fa fa-pencil"></i>编辑信息</button>'
            +'          </td>'
            +'      </tr>'

        $("#merchantsList tbody").append(s);

    }
    getAllMerchants();

})
