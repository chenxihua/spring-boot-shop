var table;
var layer;
var util;
var element;
var $;

layui.use(['layer','util', 'table' ,'element'], function(){
    layer = layui.layer;
    table = layui.table;
    util = layui.util;
    element = layui.element;
    $ = layui.$;

    table.render({
        elem: '#category',
        height: 472,
        url: '/admins/admins_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'realName',
                    title: '用户名',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'email',
                    title: '邮箱',
                    // width: 400,
                    align: 'center'
                }, {
                    field: 'phone',
                    title: '联系电话',
                    // width: 400,
                    align: 'center'
                },{
                    field: 'signTime',
                    title: '注册时间',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.signTime, 'yyyy-MM-dd HH:mm:ss'); }
                }, {
                    field: 'status',
                    title: '状态',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return idToString(d.status); }
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
        var userId = data.id;
        if (layEvent === 'update_Btn'){
            layer.open({
                type: 2
                , title: "<p style='text-align: center'>分配角色</p>"
                , area: ['450px', '550px']
                , id: 'category_dlg'
                , content: '/roles/userRoles/'+userId
                , btnAlign: 'c'
                , shade: 0.1
                , yes: function () {
                    layer.closeAll();
                }
            });
        }else if(layEvent === 'del_Btn'){
            layer.confirm('确定删除当前用户吗？', {icon: 3, title: '类别删除'}, function (index) {
                console.log(data);
                $.ajax({
                    url: "/admins/admins_delete/"+userId,
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

// 重新渲染数据表格
var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'adminName': $("#keyword").val()
        }
    });
};

// 添加管理员
var openDialog = function () {
    var url = '/admins/admins_view';
    layer.open({
        type: 2
        , title: "<p style='text-align: center'>新增管理员</p>"
        , area: ['500px', '400px']
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

var idToString = function (data) {
    var statusStr = '';
    if (data==1){
        statusStr = '<p style="color: #FF0000;">未验证</p>';
    }else if (data==2){
        statusStr = '<p style="color: #5FB878">已验证</p>';
    }
    return statusStr;
}