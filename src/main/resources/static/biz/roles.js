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
        url: '/roles/roles_list',
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
                    title: '角色名称',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'descript',
                    title: '角色描述',
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
        var roleId = data.id;
        if (layEvent === 'edit_Btn'){
            // debugger
            openDialog(data);
        }else if(layEvent === 'renew_Btn'){
            layer.open({
                type: 2
                , title: "<p style='text-align: center'>配置权限</p>"
                , area: ['450px', '580px']
                , id: 'category_dlg' // 防止重复弹出
                , content: '/roles/newRolesPer/'+roleId
                , btnAlign: 'c' // 按钮居中
                , shade: 0.1 // 不显示遮罩
                , yes: function () {
                    layer.closeAll();
                }
            });
        }else if(layEvent === 'del_Btn'){
            layer.confirm('确定删除当角色吗？', {icon: 3, title: '类别删除'}, function (index) {
                console.log(data);
                $.ajax({
                    url: "/roles/roles_delete/"+roleId,
                    type: "POST",
                    dataType: "json",
                    success: function (result) {
                        if (result == 0) {
                            obj.del();
                            layer.close(index);
                            layer.msg("删除成功", {icon: 6});
                            query();
                        } else {
                            layer.msg("删除失败", {icon: 5});
                        }
                    },
                    error: function () {
                        layer.msg("服务器出现异常，删除失败", {icon: 5});
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
            'rolesName': $("#keyword").val()
        }
    });
};

var openDialog = function (data) {
    var url = '/roles/roles_view';
    if (data != undefined) {
        url += '?rolesId=' + data.id;
    }
    layer.open({
        type: 2
        , title: "<p style='text-align: center'>角色信息</p>"
        , area: ['500px', '250px']
        , id: 'category_dlg' // 防止重复弹出
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
