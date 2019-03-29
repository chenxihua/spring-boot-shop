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
        url: '/school/school_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [
                {
                    field: 'fullName',
                    title: '学校全称',
                    // width: 400,
                    align: 'center'
                }, {
                    field: 'name',
                    title: '学校简称',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'address',
                    title: '学校地址',
                    // width: 400,
                    align: 'center'
                }, {
                    field: 'applyTime',
                    title: '申请时间',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.applyTime, 'yyyy-MM-dd HH:mm:ss'); }
                }, {
                    field: 'status',
                    title: '学校状态',
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
        var schId = data.id;
        if (layEvent === 'review_Btn'){
            layer.confirm('确定要重新审核改学校吗？',{icon: 3, title: '重新审核学校'},function (index) {
                layer.close(index);
                $.ajax({
                    url:'/school/review_school/'+schId,
                    type:'PUT',
                    dataType:'json',
                    success:function (data) {
                        if(data==0){
                            layer.msg("状态已经转换为等待审核", {icon: 6});
                            query();
                        }else{
                            layer.alert("重新审核提交出现错误，服务出现异常");
                        }
                    },
                    error:function () {
                        layer.alert("服务器出错了，请检查一下服务器");
                    }
                });
            });
        }else if(layEvent === 'check_Btn'){
            layer.open({
                type:2,
                title: "<p style='text-align: center;'>查看发布商品信息</p>",
                closeBtn: 1,
                area: ['500px', '260px'],
                shadeClose: false,
                content: '/school/toCheckSchool/'+schId,
                end: function (obj, index) {
                    // 成功之后，调用回调函数
                    query();
                }
            });
        }else if(layEvent === 'delete_Btn'){
            layer.confirm('确定要删除当前学校吗？',{icon: 3, title: '删除学校'},function (index) {
                layer.close(index);
                $.ajax({
                    url:'/school/delete_school/'+schId,
                    type:'DELETE',
                    dataType:'json',
                    success:function (data) {
                        if(data==0){
                            layer.msg("删除成功", {icon: 6});
                            query();
                        }else{
                            layer.alert("删除失败，服务出现异常");
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

var idToString = function (data) {
    var statusStr = '';
    if (data==0){
        statusStr = '<p style="color: #ee0000">审核失败</p>';
    }else if (data==1){
        statusStr = '<p style="color: dodgerblue;">审核状态</p>';
    }else if(data==2){
        statusStr = '<p style="color: #5FB878">通过审核</p>';
    }else{
        statusStr = '<p style="color: #ee0000">未知状态</p>';
    }
    return statusStr;
}

var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'schoolName': $("#keyword").val()
        }
    });
};

// 新增学校的弹出框
var openDialogSchool = function () {
    layer.open({
        type: 2
        , title: "<p style='text-align: center;'>管理员新增学校</p>"
        , area: ['600px', '420px']
        , id: 'school_dlg' // 防止重复弹出
        , content: '/school/to_add_school'
        , btnAlign: 'c' // 按钮居中
        , shade: 0.1 // 不显示遮罩
        ,end: function () {
            // 重新渲染表格
            query();
        }
    });
}
