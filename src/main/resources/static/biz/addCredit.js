layui.use(['form', 'layer', 'upload'], function () {
    var form = layui.form;
    var layer = layui.layer;
    var upload = layui.upload;
    var $ = layui.$;


    //选完文件后不自动上传
    var uploadInst = upload.render({
        elem: '#test8',
        url: '/upload/goodsPic',
        auto: false,
        accept: 'images',
        acceptMime: 'image/*',
        bindAction: '#test9',
        done: function(data){
            if (data.code==0){
                layer.msg("照片上传成功",{icon:6});
                $('#buyAddress').val(data.data);
            }else if(data.code==101){
                layer.msg(data.msg+"; 请重新上传", {icon:5});
            }else{
                layer.msg("文件上传失败，正在重新上传",{icon:5});
                uploadInst.upload();
            }
        },
        error: function () {
            top.layer.msg("服务器上传图片出错了", {icon:5,time:3000});
        }
    });


    form.on('submit(save_Btn)', function (data) {

        layer.confirm('如果卖家此次交易出现异常情况，请一定要上传证据照片，及相应描述，以此维护自己权利和信用。确定上传吗？', {icon: 3, title: '提交信用评分'}, function (index) {
            layer.close(index);

            var contextVal = data.field;
            $.ajax({
                url:'/userCredit/add_credit',
                data:contextVal,
                dataType:'json',
                type:'post',
                success: function (data) {
                    if (data==0){
                        // 1：关闭所有弹窗
                        parent.layer.closeAll();
                        // 2：提示评价信用成功
                        parent.layer.msg("已对本次交易的卖家评信用积分成功", {icon:6, time:3000});
                    }else {
                        layer.msg("本次信用评价失败", {icon:5, time:3000});
                    }
                },
                error: function () {
                    layer.msg("服务器出现异常，请检查服务器", {icon:5, time:3000});
                }
            });
        });


        return false;
    });
});
