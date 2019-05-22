var login = function(base, loginForm) {
    if ($("#username").val() == '' || $("#password").val() == '') {
        $("#errorMess").text('用户名或密码错误');
        return false;
    }
    var url = base + "/login/checkLogin.json";
    $.ajax({
        url : url,
        type : 'post',
        async: false,
        data : $(loginForm).serialize(),
        dataType : 'json',
        error : function(textStatus, errorThrown) {
            $("#errorMess").text('登录失败，请稍候再试。');
        },
        success : function(data) {
            if (data.code!=100) {
                $("#errorMess").text(data.errorMess);
            } else {
                if(data.backUrl!=null && data.backUrl!=''){
                    document.location.href =data.backUrl;
                }else{
                    document.location.href = base + "/dashboard/index.htm";

                }
            }
        }
    });
}