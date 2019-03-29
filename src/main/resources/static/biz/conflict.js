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
        elem: '#firstTime',
        type: 'datetime'
    });
    laydate.render({
        elem: '#lastTime',
        type: 'datetime'
    });

    table.render({
        elem: '#category',
        height: 472,
        url: '/userCredit/credit_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    field: 'publishUser',
                    title: '申请重新审核评分人',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return idToString(d.publishUser); }
                }, {
                    field: 'buyUser',
                    title: '评分人',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return idToString(d.buyUser); }
                }, {
                    field: 'createTime',
                    title: '申请时间',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss');}
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
        if(layEvent === 'show_Btn'){
            layer.open({
                type:2,
                title: '<p style="text-align: center; color: #009688">查看商品信息</p>',
                closeBtn: 1,
                area: ['750px', '700px'],
                shadeClose: false,
                scrollbar: true,
                content: '/userCredit/refute_Info/'+id,
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
            'firstTime': $("#firstTime").val(),
            'lastTime': $("#lastTime").val()
        }
    });
};

// 根据id，显示人的名称
var idToString = function(id){
    var userName = '';
    $.ajax({
        url:'/admins/findOne/'+id,
        type:'GET',
        async:'false',
        dataType:'json',
        success:function (data) {
            console.log("data: "+data);
            userName = data.data.realName;
            console.log("显示人民："+userName);
        }
    });
    return userName;
};