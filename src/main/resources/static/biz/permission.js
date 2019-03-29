var table;
var layer;
var element;
var $;

layui.use(['layer', 'table' ,'element'], function(){
    layer = layui.layer;
    table = layui.table;
    element = layui.element;
    $ = layui.$;

    table.render({
        elem: '#category',
        height: 472,
        url: '/permission/permission_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'name',
                    title: '权限名称',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'description',
                    title: '权限描述',
                    // width: 400,
                    align: 'center'
                }, {
                    field: 'url',
                    title: '权限请求url',
                    // width: 400,
                    align: 'center'
                }, {
                    field: 'status',
                    title: '权限状态',
                    // width: 400,
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
        var perId = data.id;
        if(layEvent === 'del_Btn'){
            layer.confirm('确定删除当权限吗？', {icon: 3, title: '类别删除'}, function (index) {
                console.log(data);
                $.ajax({
                    url: "/permission/permission_delete/"+perId,
                    type: "POST",
                    dataType: "json",
                    success: function (result) {
                        if (result == 0) {
                            obj.del();
                            layer.close(index);
                            layer.msg("删除权限成功", {icon: 6});
                            query();
                        } else {
                            layer.msg("删除权限失败", {icon: 5});
                        }
                    },
                    error:function () {
                        layer.msg("服务器出现异常，删除权限失败", {icon: 5});
                    }
                });
            });
        }
    });
});

// 表格重新渲染
var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'perName': $("#keyword").val()
        }
    });
};