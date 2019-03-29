var table;
var layer;
var util;
var element;
var $;

layui.use(['layer', 'table', 'util', 'element'], function(){
    layer = layui.layer;
    table = layui.table;
    util = layui.util;
    element = layui.element;
    $ = layui.$;

    table.render({
        elem: '#category',
        height: 472,
        url: '/goods/publish_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'name',
                    title: '商品名称',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'publishTime',
                    title: '发布时间',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.publishTime, 'yyyy-MM-dd HH:mm:ss'); }
                }, {
                    field: 'price',
                    title: '出售价格',
                    // width: 200,
                    align: 'center'
                },  {
                    field: 'status',
                    title: '商品状态',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return idToString(d.status); }
                }, {
                    title: '具体操作',
                    // width: 280,
                    align: 'center',
                    toolbar: '#tools'
                }
            ]
        ]
    });


    // 监听工具条时间
    table.on('tool(tools)', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        var id = data.id;
        if (layEvent === 'cancelBtn'){
            layer.confirm('<p style="color: red;">真的删除发布的二手商品吗？</p>',{icon: 3, title: '类别删除'},function (index) {
                layer.close(index);
                $.ajax({
                    url:'/goods/deleteById/'+id,
                    type:'DELETE',
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("删除成功", {icon: 6});
                            query();
                        } else {
                            layer.alert("<p style='color: red;'>服务器发生异常，删除错误</p>");
                        }
                    },
                    error:function () {
                        layer.alert("<p style='color: red;'>服务器出错了，请重新删除或检查一下网络</p>");
                    }
                });
            });
        }else if(layEvent === 'findBtn'){
            var paraUrl = data.id;
            layer.open({
                type:2,
                title: '<p style="text-align: center; color: #009688;">查看发布商品信息</p>',
                closeBtn: 1,
                area: ['800px', '680px'],
                shadeClose: false,
                content: '/common/getOrdersGoodsInfos/'+paraUrl,
            });
        }else if(layEvent === 'tradeBtn'){
            var goodsId = data.id;
            layer.open({
                type:2,
                title: '<p style="text-align: center; color: #009688;">查看买家交易描述</p>',
                closeBtn: 1,
                area: ['700px', '300px'],
                shadeClose: false,
                content: '/common/getTradeInfos/'+goodsId,
            });
        }else if(layEvent === 'successBtn'){
            var goodsId = data.id;
            layer.open({
                type:2,
                title: '<p style="text-align: center; color: #009688;">查看卖家购买评论</p>',
                closeBtn: 1,
                area: ['850px', '450px'],
                shadeClose: false,
                content: '/common/getSellerComments/'+goodsId,
            });
        }else if (layEvent === 'reUpBtn'){
            var goodsId = data.id;
            layer.confirm('确定要重新上传吗？',{icon: 3, title: '数据重传'},function (index) {
                layer.close(index);
                $.ajax({
                    url:'/goods/updateGoods/'+goodsId,
                    type:'PUT',
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("重新上传成功，等待管理员重新审核", {icon: 6});
                            query();
                        }else {
                            layer.alert("重新上传失败，服务器出现异常");
                        }
                    },
                    error:function () {
                        layer.alert("服务器出错了，请重新删除或检查一下网络");
                    }
                });
            });
        }
    });

});

var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'goodsName': $("#keyword").val()
        }
    });
};

// 显示订单状态数字转为文字
var idToString = function(statusId) {
    var statusStr = '';
    if (statusId==0){
        statusStr = '审核不通过';
    }else if (statusId==1){
        statusStr = '等待审核';
    }else if(statusId==2){
        statusStr = '正在出售';
    }else if(statusId==3){
        statusStr = '已被下订';
    }else if(statusId==4){
        statusStr = '出售成功';
    }else{
        statusStr = '未知状态';
    }
    return statusStr;
}

var batch_delete = function () {
    layer.confirm('<p style="color: red;">确定要删除所选发布的二手商品吗？</p>', function (index) {
        layui.use(['layer'], function(){
            var layer = layui.layer;
            var $  = layui.$;

            var checkStatus = table.checkStatus('category');
            if(checkStatus.data.length==0){
                parent.layer.msg('请先选择要删除的数据行！', {icon: 2});
                return ;
            }
            var ids = "";
            for(var i=0;i<checkStatus.data.length;i++){
                ids += checkStatus.data[i].id+"-";
            }
            $.post('/goods/batch_delete', {'ids':ids}, function (data) {
                if(data==0){
                    layer.msg("删除成功", {icon: 6,time:2000,shade:0.2});
                    query();
                }else{
                    layer.msg("批量删除失败", {icon: 5,time:3000,shade:0.2});
                    query();
                }
            });
        });
    });
}