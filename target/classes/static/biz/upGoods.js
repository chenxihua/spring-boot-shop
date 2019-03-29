
layui.use(['layer', 'element', 'upload', 'form'], function() {
    var layer = layui.layer;
    var element = layui.element;
    var upload = layui.upload;
    var form = layui.form;
    var $ = layui.$;

    loadUploadSelected();

    function loadUploadSelected(){
        // 合二而一的方法，一起加载两个数据;
        loadAllDatas();
        // 重新渲染表单
        form.render(null,'addGoodsForm');
    }
    
    function loadAllDatas() {
        $.ajax({
            url: '/items/getSelectedDates',
            type: 'post',
            dataType: 'json',
            async:false,
            success: function (data) {
                console.log("code: "+data.code);
                if (data.code==0){
                    var htmlItem = "";
                    var htmlSch = "";

                    $("#typeSelected").html("");
                    $("#schoolSelected").html("");

                    var goodTypes = data.itemDatas;
                    htmlItem += "<option value=''>直接选择或者搜索</option>";
                    var lens = goodTypes.length;
                    for (i=0; i<lens; i++) {
                        htmlItem += "<option value='"+goodTypes[i].id+"'>"+goodTypes[i].name+"</option>";
                    }

                    var publishSchs = data.schoolDatas;
                    htmlSch += "<option value=''>直接选择或者搜索</option>";
                    var lens = publishSchs.length;
                    for (i=0; i<lens; i++) {
                        htmlSch += "<option value='"+publishSchs[i].id+"'>"+publishSchs[i].fullName+"</option>";
                    }

                    $("#typeSelected").html(htmlItem);
                    $("#schoolSelected").html(htmlSch);

                }else {
                    layer.alert("加载数据失败，无法响应");
                }
            },
            error: function () {
                layer.alert("无法加载下拉框数据，服务器出现异常。")
            }
        });
        
    }


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
                layer.msg("图片上传成功",{icon:6});
                $('#picAddress').val(data.data);
            }else {
                layer.msg("图片上传失败，正在重新上传",{icon:5});
                uploadInst.upload();
            }
        },
        error: function () {
            top.layer.msg("服务器上传图片出错了", {icon:5,time:3000});
        }
    });

    //监听提交
    form.on('submit(upGoodsBtn)', function(data){
        if (document.getElementById('picAddress').value==''){
            layer.alert("操作异常，你还没有上传图片，请上传图片再发布商品");
            return false;
        }else {
            var formDate = data.field;
            $.ajax({
                url: '/goods/saveGoodInfos',
                type: 'POST',
                data: formDate,
                dataType: 'json',
                success: function (data) {
                    if (data==0){
                        layer.alert("商品发布成功了，等待管理员审核");
                        // 清空表单
                        $("#addGoodsForm")[0].reset();
                        $("#picAddress").val("");
                        layui.form.render();
                    }else{
                        layer.alert("商品发布失败，请重新发布吧");
                        // 清空表单
                        $("#addGoodsForm")[0].reset();
                        $("#picAddress").val("");
                        layui.form.render();
                    }
                },
                error:function () {
                    layer.alert("服务器出错，请检查网络再上传二手商品");
                }
            });
            return false;
        }
    });
});