var table;
var layer;
var element;
var util;
var $;

layui.use(['layer', 'table', 'util','element'], function(){
    layer = layui.layer;
    table = layui.table;
    element = layui.element;
    util = layui.util;
    $ = layui.$;

    table.render({
        elem: '#category',
        height: 472,
        url: '/goods/goods_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [
                {
                    field: 'name',
                    title: '商品名称',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'price',
                    title: '商品价格',
                    // width: 400,
                    align: 'center'
                },  {
                    field: 'description',
                    title: '商品描述',
                    // width: 400,
                    align: 'center'
                }, {
                    field: 'publishTime',
                    title: '发布时间',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.publishTime, 'yyyy-MM-dd HH:mm:ss'); }
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
        var id = data.id;
        if (layEvent === 'yes_Btn'){
            openDialog(id);
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

var openDialog = function (id) {
    layer.confirm('即将跳转到商品详细信息页面，确定要继续吗？', function (index) {
        window.location.href='/common/checkGoodInfo/'+id;
    });

};