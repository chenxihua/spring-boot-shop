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
        accept: 'file',
        acceptMime: 'file/*',
        bindAction: '#test9',
        done: function(data){
            if (data.code==0){
                layer.msg("文件上传成功",{icon:6});
                $('#fileAddress').val(data.data);
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


    form.on('submit(*)', function (data) {
        var index = layer.msg('提交中，请稍候', {icon: 16, time: false, shade: 0.8});
        var context = data.field;
        var url = "/notices/notices_add";

        $.ajax({
            url: url,
            data: context,
            dataType: "text",
            success: function (data) {
                var index = parent.layer.getFrameIndex(window.name); //获取窗口索引
                parent.layer.close(index);  // 关闭layer

                if (data == 0) {
                    parent.layer.msg("操作成功", {icon: 6});
                    parent.table.reload('category', {
                        page: {
                            curr: 1 // 重新从第1页开始
                        },
                        where: {
                            'keyword': $("#keyword").val()
                        }
                    });
                } else {
                    parent.layer.msg("操作失败", {icon: 5});
                }
            }, error: function () {
                parent.layer.msg("操作失败", {icon: 5});
            }
        });
        return false;
    });
});

// 退出按钮
var exitBtn = function () {
    layui.use([ 'layer', 'form' ], function() {
        var layer = layui.layer;
        var form = layui.form;

        var index = parent.layer.getFrameIndex(window.name);
        parent.layer.close(index);
    });
}

// 下载附件
var downloadTxt = function () {
    var id = $('#noticesId').val();
    $.ajax({
        url:'/notices/download_file/'+id,
        type:'post',
        dataType:'json',
        success: function (data) {
            if (data==200){
                parent.layer.msg("附件成功保存到C:\\chenxihua,目录下面了。", {icon:6, time:4000});
            } else if(data==0){
                parent.layer.alert("该公告无附件，操作错误",{icon:5});
            }else{
                parent.layer.msg("附件下载失败", {icon: 5});
            }
        },
        error: function () {
            parent.layer.msg("服务器出现错误，下载附件失败",{icon:5});
        }
    });
}