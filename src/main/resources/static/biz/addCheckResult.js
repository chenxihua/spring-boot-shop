
layui.use([ 'layer', 'element', 'form' ], function() {
    var layer = layui.layer;
    var element = layui.element;
    var form = layui.form;
    var $ = layui.$;

    // 表单监听事件
    form.on('submit(save_yes)', function(data){
        var formDate = data.field;
        var yesBtn = 2;
        $.ajax({
            url: '/school/checkResults/'+yesBtn,
            type: 'POST',
            data: formDate,
            dataType: 'json',
            success: function (data) {
                if(data==0){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                }else {
                    parent.layer.alert("更新状态出现异常,不能更新");
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                }
            },
            error: function () {
                parent.layer.alert("服务器程序出错了，请检查服务器，再操作！");
            }
        });
        return false;
    });

    // 表单监听事件
    form.on('submit(save_no)', function(data){
        var formDate = data.field;
        var yesBtn = 0;
        $.ajax({
            url: '/school/checkResults/'+yesBtn,
            type: 'POST',
            data: formDate,
            dataType: 'json',
            success: function (data) {
                if(data==0){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                }else {
                    parent.layer.alert("更新状态出现异常,不能更新");
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                }
            },
            error: function () {
                parent.layer.alert("服务器程序出错了，请检查服务器，再操作！");
            }
        });
        return false;
    });
});