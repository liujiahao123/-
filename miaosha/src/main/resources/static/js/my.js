var pas_salt = "1a2b3c4d5";
function login() {
    $("#loginform").validate({
        submitHandler: function(form) {
            doLogin();
        }
    });
}
function doLogin() {
    var inputpassword = $("#password").val();
    var salt = pas_salt;
    var str = "" + salt.charAt(0) + salt.charAt(5) + inputpassword + salt.charAt(3) + salt.charAt(6);
    var password = $.md5(str);
    $.ajax({
        url: "/dologin",
        type: "POST",
        data: {
            mobile: $("#mobile").val(),
            password: password
        },
        success: function(data) {
            console.log(data);
            if (data.code == 0) {
                window.location.href = "/togoodslist";
            } else {
                layer.msg(data.mes);
            }
        },
        error: function(data) {
            console.log(data);
        }
    });
}
function logout() {
    layer.confirm('确定要退出系统吗？', {
        btn: ['确定', '取消'] //按钮
    },
    function() {
        $.ajax({
            url: "/logout",
            type: "POST",
            success: function(data) {
                if (data == "true") {
                    window.location.href = "/tologin";
                }
            },
            error: function(data) {
                console.log(data);
            }
        });
    },
    function() {

});
}