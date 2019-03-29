var table;
var layer;
var util;
var element;
var $;

layui.use(['layer', 'table', 'util' ,'element'], function(){
    layer = layui.layer;
    table = layui.table;
    util = layui.util;
    element = layui.element;
    $ = layui.$;

    table.render({
        elem: '#category',
        height: 472,
        url: '/notices/notices_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'title',
                    title: '公告标题',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'createTime',
                    title: '公告创建时间',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.createTime, 'yyyy-MM-dd HH:mm:ss'); }
                }, {
                    field: 'contents',
                    title: '公告内容',
                    // width: 500,
                    align: 'center'
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
            if (layEvent === 'update_Btn') {
                updataDialog(id);
            }
        });
});

// 重新渲染数据表格
var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'keyword': $("#keyword").val()
        }
    });
};

//  新增
var openDialog = function () {
    var url = '/notices/new_notices';
    layer.open({
        type: 2
        , title: "<p style='text-align: center'>公告信息</p>"
        , area: ['600px', '350px']
        , id: 'category_add' // 防止重复弹出
        , content: url
        , btnAlign: 'c' // 按钮居中
        , shade: 0.1 // 不显示遮罩
        , yes: function () {
            layer.closeAll();
        },
        success: function (obj, index) {
            // console.log(obj, index);
        }
    });
};

//  更新公告页面
var updataDialog = function (id) {
    var url = '/notices/updata_notices/'+id;
    layer.open({
        type: 2
        , title: "<p style='text-align: center'>公告信息</p>"
        , area: ['600px', '300px']
        , id: 'category_dlg' // 防止重复弹出
        , content: url
        , btnAlign: 'c' // 按钮居中
        , shade: 0.1 // 不显示遮罩
        , yes: function () {
            layer.closeAll();
        },
        success: function (obj, index) {
        }
    });
};


var bitchDelete = function () {
    layer.confirm('确定要删除所选公告吗？', function (index) {
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
            $.post('/notices/batch_delete', {'ids':ids}, function (data) {
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
