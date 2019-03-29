var table;
var layer;
var element;
var $;

layui.use(['layer', 'table' ,'element'], function(){
    layer = layui.layer;
    table = layui.table;
    element = layui.element;
    $ = layui.$;

    var rolesVal = $('#getRolesId').val();

    table.render({
        elem: '#category',
        id: 'idTest',
        height: 380,
        url: '/roles/showPermiss/'+rolesVal,
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'permissionId',
                    title: '权限名称',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return idToString(d.permissionId); }
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
        if(layEvent === 'del_Btn'){
            layer.confirm('确定删除角色的这个权限吗？', {icon: 3, title: '类别删除'}, function (index) {
                console.log(data);
                $.ajax({
                    url: "/roles/delete_roles_permiss/"+id,
                    type: "DELETE",
                    dataType: "json",
                    success: function (result) {
                        if (result == 0) {
                            obj.del();
                            layer.close(index);
                            layer.msg("删除成功", {icon: 6});
                            table.reload('idTest');
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

    // 获取权限名称
    function idToString(permissionId){
        var PermissionName = '';
        $.ajax({
            url:'/roles/permiss_name/'+permissionId,
            type:'GET',
            async:false,
            dataType:'json',
            success:function (data) {
                PermissionName = data.data.name;
            }
        });
        return PermissionName;
    };
});

// 关闭弹出层
var exitBtn = function () {
    //当你在iframe页面关闭自身时
    var index = parent.layer.getFrameIndex(window.name);
    parent.layer.close(index);
}

// 新分配权限
var addPer = function () {
    var permissId = $('#permissSelect').val();
    var rolesId = $('#getRolesId').val();

    if(permissId==''){
        parent.layer.alert("你选择的权限为空，请选择一个权限",{icon: 3, title: '类别删除'});
    }else {
        $.ajax({
            url: '/roles/addForPermiss',
            type: 'post',
            data: {'perId':permissId, 'rolesId': rolesId},
            dataType: 'json',
            success: function (data) {
                if(data==1){
                    parent.layer.alert("当前角色已经分配了这个权限，请重新选择",{icon: 3, title: '类别删除'});
                }else if(data==0) {
                    parent.layer.alert("分配权限成功，请继续操作",{icon: 3, title: '类别删除'});
                    table.reload('idTest');
                }else{
                    parent.layer.alert("服务端出问题，请检查服务再操作",{icon: 3, title: '类别删除'});
                }
            },
            error: function () {
                parent.layer.alert("服务端出异常了，请检查再点击操作");
            }
        });
    }

}