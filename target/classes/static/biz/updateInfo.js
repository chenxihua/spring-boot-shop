
layui.use([ 'layer', 'form' ], function() {
    var layer = layui.layer;
    var form = layui.form;
    var $ = layui.$;


    // 表单监听事件
    form.on('submit(save_btn)', function(data){
        var formDate = data.field;
        $.ajax({
            url: '/user/updateBtn',
            type: 'post',
            data: formDate,
            dataType: 'json',
            success: function (data) {
                if(data==0){
                    // 关闭窗口
                    parent.layer.closeAll();
                    // 提示下订成功
                    parent.layer.msg("更新个人信息完毕", {icon: 6,time:3000});
                }else {
                    layer.alert("服务器出错了，请重新更新");
                }
            },
            error: function () {
                layer.alert("服务器程序出错了，请检查服务器，再操作！");
            }
        });
        return false;
    });
});