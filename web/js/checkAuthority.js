$(function(){
    //var logined=false;
    function getQueryString(name) {
        var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    }
    var authorid=getQueryString('authorid')

    $.ajax({
        url:"/ketuan/authoritys/getAuthorById",
        data:{
            authorityid:authorid
        },
        success:function (res) {
            console.log('单独的authorityinfo',res.data)
            var adminAuthorarr=JSON.parse(res.data.authority.adminAuthor)
            console.log(adminAuthorarr)
            for(let i=0;i<adminAuthorarr.length;i++){
                var adminele=adminAuthorarr[i]
                if (adminele=='1'){
                    $('#usersmanage').removeClass('hideview')
                }
                if (adminele=='6'){
                    console.log('执行了')
                    $('#merchantsmanage').removeClass('hideview')
                }
                if (adminele=='11'){
                    $('#productsmanage').removeClass('hideview')
                }
                if (adminele=='26'){
                    $('#chatmanage').removeClass('hideview')
                }
                if (adminele=='16'){
                    $('#groupbuysmanage').removeClass('hideview')
                }
                if (adminele=='21'){
                    $('#ordersmanage').removeClass('hideview')
                }
                if (adminele=='28'){
                    $('#expressagesmanage').removeClass('hideview')
                }
                if (adminele=='34'){
                    $('#marketmanage').removeClass('hideview')
                }
                if (adminele=='38'){
                    $('#authoritymanage').removeClass('hideview')
                }

            }
        }
    })

})