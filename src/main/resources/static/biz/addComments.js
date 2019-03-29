
layui.use(['form', 'layer'], function(){
    var form = layui.form;
    var layer = layui.layer;
    var $ = layui.$;

    //点击保存事件
    form.on('submit(saveComments)',function(data){
        var formdata = data.field;
        $.ajax({
            url:'/comments/saveComments',
            type:'post',
            data:formdata,
            dataType:'json',
            success:function (data) {
                if (data==0){
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.layer.close(index);
                    parent.layer.msg("添加评论成功", {icon: 6});
                }else {
                    parent.layer.alert("保存评论出错，请检查一下网络");
                }
            },
            error:function () {
                parent.layer.alert("服务器出错，请重新操作");
            }
        });
        return false;
    });
});