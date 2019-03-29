var table;
var layer;
var laydate;
var util;
var element;
var $;

layui.use(['layer', 'table', 'laydate', 'util', 'element'], function(){
    layer = layui.layer;
    table = layui.table;
    laydate = layui.laydate;
    util = layui.util;
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
        url: '/collect/collect_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'goodsId',
                    title: '商品名称',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return idToString(d.goodsId); }
                }, {
                    field: 'collectTime',
                    title: '收藏时间',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.collectTime, 'yyyy-MM-dd HH:mm:ss'); }
                }, {
                    title: '操作',
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
        if (layEvent === 'showBtn'){
            var paraUrl = data.goodsId;
            window.location.href= '/common/getGoodInfo/'+paraUrl;
        }else if(layEvent === 'delBtn'){
            var paraUrl = data.id;
            layer.confirm('真的删除当前收藏吗？', {icon: 3, title: '类别删除'}, function(index){
                obj.del();
                layer.close(index);
                $.ajax({
                    url:'/collect/deleteById/'+paraUrl,
                    type:'DELETE',
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("删除成功", {icon: 6});
                            query();
                        }else {
                            layer.alert("注意，服务器出现异常，删除失败", {icon:5})
                        }
                    },
                    error:function () {
                        layer.alert("服务器出错，检查网络后再操作吧");
                    }
                });
            });

        }
    });
});

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
}

var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'firstTime': $("#test1").val()
        }
    });
};

var batchDelete = function () {
    layer.confirm('确定要删除所选收藏的二手商品吗？', function (index) {
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
            $.post('/collect/batch_delete', {'ids':ids}, function (data) {
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