var table;
var layer;
var util;
var laydate;
var element;
var $;

layui.use(['layer', 'table', 'laydate','util', 'element'], function(){
    layer = layui.layer;
    table = layui.table;
    util = layui.util;
    laydate = layui.laydate;
    element = layui.element;
    $ = layui.$;

    //常规用法
    laydate.render({
        elem: '#test1',
        type: 'datetime'
    });
    laydate.render({
        elem: '#test2',
        type: 'datetime'
    });

    table.render({
        elem: '#category',
        height: 472,
        url: '/orders/orders_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'orderUuid',
                    title: '订单编号',
                    width: 200,
                    align: 'center'
                }, {
                    field: 'goodsId',
                    title: '商品名称',
                    width: 200,
                    align: 'center',
                    templet:function(d){ return idToString(d.goodsId); }
                }, {
                    field: 'startTime',
                    title: '下单时间',
                    width: 200,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.startTime, 'yyyy-MM-dd HH:mm:ss');}
                }, {
                    field: 'status',
                    title: '订单状态',
                    width: 190,
                    align: 'center',
                    templet:function(d) { return statusToString(d.status); }
                }, {
                    title: '操作',
                    width: 320,
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
            layer.confirm('<p style="color: red;">真的取消订单吗？</p>',{icon: 3, title: '取消处理'}, function (index) {
                layer.close(index);
                $.ajax({
                    url:'/orders/updateOrders',
                    type:'PUT',
                    data:{'id': id},
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("取消订单成功", {icon: 6});
                            query();
                        }else {
                            top.layer.msg("服务器出错，请等会再操作！",{icon: 5}, {time: 3000});
                        }
                    },
                    error:function () {
                        layer.alert("服务器出现异常，请检查服务器再操作！");
                    }
                });
            });
        }else if(layEvent === 'successBtn'){
            layer.confirm('<p style="color: #009688;">确认交易成功？</p>',{icon: 3, title: '交易处理'},function (index) {
                layer.close(index);
                $.ajax({
                    url:'/orders/updateSuccess',
                    type:'PUT',
                    data:{'id': id},
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("交易成功", {icon: 6});
                            query();
                        }else {
                            top.layer.msg("服务器出错，请等会再操作！",{icon: 5}, {time: 3000});
                        }
                    },
                    error:function () {
                        layer.alert("服务器出现异常，请检查服务器再操作！");
                    }
                });
            });
        }else if(layEvent === 'findBtn'){
            var paraUrl = data.goodsId;
            layer.open({
                type:2,
                title: '<p style="font-size: 18px; text-align: center; color: #009688">查看商品信息</p>',
                closeBtn: 1,
                area: ['800px', '680px'],
                shadeClose: false,
                scrollbar: true,
                content: '/common/getOrdersGoodsInfos/'+paraUrl,
            });
        }else if(layEvent === 'discussBtn'){
            var orderId = data.id;
            layer.confirm('要进行订单评论吗？',{icon: 3, title: '评论'},function (index) {
                layer.close(index);
                window.location.href="/common/discuss/"+orderId;
            });
        }else if(layEvent === 'deleteBtn'){
            layer.confirm('<p style="color: red;">真的删除订单记录吗？</p>',{icon: 3, title: '类别删除'}, function (index) {
                layer.close(index);
                $.ajax({
                    url:'/orders/deleteOrders/'+id,
                    type:'DELETE',
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("删除成功", {icon: 6});
                            query();
                        }else {
                            top.layer.msg("服务器出错，请等会再操作！",{icon: 5}, {time: 3000});
                        }
                    },
                    error: function () {
                        layer.alert("服务器出现异常，请检查服务器再操作！");
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
            'startTime': $("#test1").val(),
            'endTime': $("#test2").val()
        }
    });
};

// 获取商品名称
var idToString = function(goodsId){
    var goodName = '';
    $.ajax({
        url:'/goods/findById/'+goodsId,
        type:'GET',
        async:false,
        dataType:'json',
        success:function (data) {
            goodName = data.data.name;
        }
    });
    return goodName;
};

// 显示订单状态数字转为文字
var statusToString = function(statusId) {
    var statusStr = '';
    if (statusId==1){
        statusStr = '正在交易';
    }else if(statusId==2){
        statusStr = '交易完成';
    }else if(statusStr==0){
        statusStr = '订单取消';
    }else {
        statusStr = '未知状态';
    }
    return statusStr;
}