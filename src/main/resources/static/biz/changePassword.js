
// 更改密码
layui.use([ 'layer', 'element', 'form' ], function() {
    var layer = layui.layer;
    var element = layui.element;
    var form = layui.form;
    var $ = layui.$;

    // 表单检验
    form.verify({
        pass: [/^[\S]{6,12}$/, '密码必须6到12位，且不能出现空格'],
    });

    //监听提交
    form.on('submit(updatePassBtn)', function(data){

        var oldPass = $('#oldPass').val();
        var newPass = $('#newPass').val();
        var reConfirmPass = $('#reConfirm').val();

        if (newPass!=reConfirmPass){
            layer.alert("输入的两次新密码不一致，请重新输入");
            // 清空表单
            $("#example")[0].reset();
            layui.form.render();
        }else{
            layer.confirm('修改密码之后，要重新登录，是否要继续修改？',function (index) {
                layer.close(index);
                $.ajax({
                    url:'/user/alterPassword',
                    type:'POST',
                    data:{'oldPass':oldPass, 'newPass':newPass},
                    dataType:'json',
                    success: function (data) {
                        if (data.code==0) {
                            window.location.href='/common/logout';
                        }else if(data.code==1){
                            layer.alert("修改密码错误，发生未知错误")
                            $("#example")[0].reset();
                            layui.form.render();
                        }else if(data.code==2){
                            layer.alert("修改密码错误，输入的原密码不正确")
                            $("#example")[0].reset();
                            layui.form.render();
                        }
                    },
                    error: function () {
                        layer.alert("服务器出错，请检查网络再修改密码");
                    }
                });
            });
        }
        return false;
    });

});