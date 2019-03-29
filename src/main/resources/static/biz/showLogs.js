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
        url: '/logs/sysLogs_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    fixed: 'left',
                    type: 'checkbox'
                }, {
                    field: 'username',
                    title: '操作用户名',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'operation',
                    title: '具体操作',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'method',
                    title: '方法名',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'params',
                    title: '方法参数',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'ip',
                    title: '访问ip',
                    // width: 200,
                    align: 'center'
                },{
                    field: 'uri',
                    title: '访问URL',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'createDate',
                    title: '访问时间',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return util.toDateString(d.createDate, 'yyyy-MM-dd HH:mm:ss'); }
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
        var paraUrl = data.id;
        if(layEvent === 'del_Btn'){
            layer.confirm('真的删除当前日志吗？', {icon: 3, title: '类别删除'}, function(index){
                obj.del();
                layer.close(index);
                $.ajax({
                    url:'/logs/delete_logs/'+paraUrl,
                    type:'DELETE',
                    dataType:'json',
                    success:function (data) {
                        if (data==0){
                            layer.msg("删除成功", {icon: 6});
                            query();
                        }else{
                            layer.alert("请注意：删除失败");
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


var query = function () {
    table.reload('category', {
        page: {
            curr: 1 // 重新从第1页开始
        },
        where: {
            'keyword': $("#keyword").val(),
            'firstTime': $("#firstTime").val(),
            'lastTime': $("#lastTime").val()
        }
    });
};

// 批量删除
var batch_delete = function () {
    layer.confirm('确定批量删除当前日志吗？', {icon: 3, title: '类别删除'}, function(index) {
        layer.close(index);
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
            $.post('/logs/batch_delete', {'ids':ids}, function (data) {
                if(data==0){
                    layer.msg("批量删除成功", {icon: 6,time:2000,shade:0.2});
                    query();
                }else{
                    layer.msg("批量删除失败", {icon: 5,time:3000,shade:0.2});
                    query();
                }
            });
        });
    });

}