$(function () {

    $("#loading-example-btn").on("click", function (e) {
        getAllGroups();
    })

    function formatDate(now) {
        var year = now.getFullYear();
        var month = now.getMonth() + 1;
        var date = now.getDate();
        var hour = now.getHours();
        var minute = now.getMinutes();
        var second = now.getSeconds();
        return year + "-" + month + "-" + date + "   " + hour + ":" + minute + ":" + second;
    }

    $("#searchBtn").on("click", function (e) {
        var key = $("#searchKey")[0].value;
        if (key == "" || key == undefined) {
            getAllGroups();
            return
        }
        $.ajax({
            url: "/ketuan/backmanage/groupsearch?key=" + key,
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("搜索团购失败！" + data["failedMessage"]);
                }
                else {
                    var groups = data["data"]["groups"];
                    $("#groupList tbody").html("");
                    for (var i = 0; i < groups.length; i++) {
                        appendTr(groups[i]);
                    }
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("搜索团购失败！" + JSON.stringify(e));
            }
        })
    })

    function getAllGroups() {
        $.ajax({
            url: "/ketuan/backmanage/grouplistall",
            type: "GET",
            dataType: "json",
            success: function (data) {
                console.info(JSON.stringify(data));
                if (data["failed"]) {
                    alert("获取团购失败！" + data["failedMessage"]);
                }
                else {
                    var groups = data["data"]["groups"];
                    $("#groupList tbody").html("");
                    for (var i = 0; i < groups.length; i++) {
                        appendTr(groups[i]);
                        showOnoff();
                        onlineGroup();
                        offlineGroup();
                        setA_btn();
                    }
                }
            },
            error: function (e) {
                console.error(JSON.stringify(e));
                alert("获取团购失败！" + JSON.stringify(e));
            }
        })
    }

    function appendTr(group) {
        var state = "团购未上线";
        var labeltype = "label-default";
        switch (group["groupState"]) {
            case 0:
                state = "团购未上线";
                break;
            case 1:
                state = "团购进行中";
                labeltype = "label-primary";
                break;
            case 2:
                state = "团购已结束";
                break;
        }
        var type = "固定团";
        if (group["groupType"] == 1) {
            type = "动态团";
        }
        var offlinetime = formatDate(new Date(group["endTime"]));
        var s = '<tr>'
            + '          <td class="project-status">'
            + '             <span class="label ' + labeltype + '">' + state
            + '          </td>'
            + '          <td class="project-title">'
            + '              <a class="a_btn" href="javascript:;" h_id="groupbuys_detail.html?groupid=' + group["id"] + '">' + group["groupName"]
            + '              </a>'
            + '              <br/>'
            + '              <small>下线时间：' + offlinetime + '</small>'
            + '          </td>'
            + '          <td class="project-completion">'
            + '              <small>成团数量:</small>' + group["groupCount"]
            + '              <br/>'
            + '              <small>当前参团数:</small>' + group["offeredCount"]
            + '          </td>'

            + '         <td class="project-people">'
            + '              <small>团购类型</small>'
            + '              <span class="label label-primary">' + type + '</span>'
            + '          </td>'
            + '          <td class="project-actions">'
            + '              <span groupState=' + group["groupState"] + ' class="btn btn-outline btn-success online"  groupid=' + group["id"] + '><i class="fa fa-pencil"></i> 上线 </span>'
            + '              <span groupState=' + group["groupState"] + ' class="btn btn-outline btn-danger offline" groupid=' + group["id"] + '><i class="fa fa-pencil"></i> 下线 </span>'
            + '          </td>'
            + '      </tr>'
        /*$("#groupList tbody").html("");*/
        $("#groupList tbody").append(s);
    }

    getAllGroups();

    function showOnoff() {
        for (let i = 0; i < $('tr').length; i++) {
            if ($('.project-actions .online')[i].getAttribute('groupState') == 0) {
                $('.project-actions .offline')[i].style.display = 'none'
            } else if ($('.project-actions .online')[i].getAttribute('groupState') == 1) {
                $('.project-actions .online')[i].style.display = 'none'
            }
        }
    }

    function onlineGroup() {
        $('.project-actions .online').unbind("click").click(function () {
            console.log('111111111111111调用onlineGroup')
            var groupid = $(this)[0].getAttribute('groupid')
            $.ajax({
                url: '/ketuan/backmanage/changeGroupstate',
                data: {
                    groupState: '1',
                    groupId: groupid
                },
                success: function (data) {
                    if (data["failed"]) {
                        alert("团购上线失败");
                    }
                    else {
                        alert("商品已成功上线");
                    }
                },
                error: function (e) {
                    console.error(JSON.stringify(e));
                    alert("团购上线失败！" + JSON.stringify(e));
                }
            })

        })
    }

    function offlineGroup() {
        $('.project-actions .offline').unbind("click").click(function () {
            console.log('22222222222调用offlineGroup')
            var groupid = $(this)[0].getAttribute('groupid')
            $.ajax({
                url: '/ketuan/backmanage/changeGroupstate',
                data: {
                    groupState: '0',
                    groupId: groupid
                },
                success: function (data) {
                    if (data["failed"]) {
                        alert("团购下线失败");
                    }
                    else {
                        alert("商品已成功下线");
                    }
                },
                error: function (e) {
                    console.error(JSON.stringify(e));
                    alert("团购下线失败！" + JSON.stringify(e));
                }
            })
        })
    }
function setA_btn(){
    $('.a_btn').unbind('click').on('click', function () {
        var detailurl=$(this)[0].getAttribute('h_id')
        console.log('detailurl',detailurl)
        page(detailurl,'团购详情')
    })
}


    function page(url, title) {
        var nav = $(window.parent.document).find('.J_menuTabs .page-tabs-content ');
        $(window.parent.document).find('.J_menuTabs .page-tabs-content ').find(".J_menuTab.active").removeClass("active");
        $(window.parent.document).find('.J_mainContent').find("iframe").css("display", "none");
        var iframe = '<iframe class="J_iframe" name="iframe10000" width="100%" height="100%" src="' + url + '" frameborder="0" data-id="' + url
            + '" seamless="" style="display: inline;"></iframe>';
        $(window.parent.document).find('.J_menuTabs .page-tabs-content ').append(
            ' <a href="javascript:;" class="J_menuTab active" data-id="' + url + '">' + title + ' <i class="fa fa-times-circle"></i></a>');
        $(window.parent.document).find('.J_mainContent').append(iframe);
    }

})
