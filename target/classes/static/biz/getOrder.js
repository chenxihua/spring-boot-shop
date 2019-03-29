
layui.use([ 'layer','layedit', 'form' ], function() {
    var layer = layui.layer;
    var layedit = layui.layedit;
    var form = layui.form;
    var $ = layui.$;

    layedit.build('demo',{
        tool: ['left', 'center', 'right', '|', 'face']
    });

    // 表单监听事件
    form.on('submit(saveOrderBtn)', function(data){
        var formDate = data.field;
        $.ajax({
            url: '/orders/saveOrdersByNew',
            type: 'POST',
            data: formDate,
            dataType: 'json',
            success: function (data) {
                if(data.success){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    // 提示下订成功
                    parent.layer.msg("下订成功，请与卖家联系", {icon: 6,time:3000});
                }else {
                    layer.alert("服务器出错了，请重新下订");
                }
            },
            error: function () {
                layer.alert("服务器程序出错了，请检查服务器，再操作！");
            }
        });
        return false;
    });
});