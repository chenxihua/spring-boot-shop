
layui.use([ 'layer', 'element' ], function() {
    var layer = layui.layer;
    var element = layui.element;
    var $ = layui.$;

    var goodsId = $('#goodId').val();


    // 点击返回事件
    $("#returnRrePgae").click(function(){
        window.history.back(-1);  // 返回历史窗口
    });

    // 点击审核通过
    $('#yes_btn').click(function () {
        layer.confirm('商品审核确认能通过？', function (index) {
            layer.close(index);
            $.ajax({
                url: '/goods/passCheck/'+goodsId,
                dataType: 'json',
                type: 'POST',
                success: function (result) {
                    if(result ==0){
                        layer.msg("审核通过",{icon: 6});
                        $('#yes_btn').prop('disabled', true);
                        $('#no_btn').prop('disabled', true);
                        $('#yes_btn').attr('class', 'layui-btn layui-btn-disabled');
                        $('#no_btn').attr('class', 'layui-btn layui-btn-disabled');
                    }else{
                        layer.alert("审核程序出现问题, 确定退出？");
                    }
                },
                error: function () {
                    layer.alert("服务器出现异常，请检查服务器再操作！");
                }
            });
        });
    });

    // 点击禁止通过按钮
    $('#no_btn').click(function () {
        layer.confirm('商品审核确认能通过？', function (index) {
            layer.close(index);
            $.ajax({
                url: '/goods/defineCheck/'+goodsId,
                dataType: 'json',
                type: 'POST',
                success: function (result) {
                    if(result==0){
                        layer.msg("禁止通过",{icon: 5});
                        $('#yes_btn').prop('disabled', true);
                        $('#no_btn').prop('disabled', true);
                        $('#yes_btn').attr('class', 'layui-btn layui-btn-disabled');
                        $('#no_btn').attr('class', 'layui-btn layui-btn-disabled');
                    }else{
                        layer.alert("审核程序出现问题, 确定退出？");
                    }
                },
                error: function () {
                    layer.alert("服务器出现异常，请检查服务器再操作！");
                }
            });
        });
    });

});