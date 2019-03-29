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
        url: '/admins/users_list',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [
                {
                    field: 'realName',
                    title: '用户名',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'account',
                    title: '用户昵称',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'sex',
                    title: '性别',
                    // width: 200,
                    align: 'center',
                    templet:function(d){ return sexToString(d.sex); }
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
                    templet:function(d){ return statusToString(d.status); }
                }, {
                    field: 'type',
                    title: '人员类型',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return typeToString(d.type); }
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
        if (layEvent === 'show_btn'){
            layer.alert("功能正待完善，请稍等");
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


var sexToString = function (data) {
    var sexVal = "";
    if (data==1){
        sexVal="男";
    } else if (data==0){
        sexVal="女";
    } else {
        sexVal="未知性别";
    }
    return sexVal;
}

var statusToString = function (data) {
    var statusVal = "";
    if (data==1){
        statusVal="未验证";
    } else if(data==2){
        statusVal="已验证";
    }else {
        statusVal="未知状态";
    }
    return statusVal;
}

var typeToString = function (data) {
    var typeVal = "";
    if(data==1){
        typeVal="学生";
    }else if (data==2){
        typeVal="老师";
    } else if(data==3){
        typeVal="校外人员";
    }else{
        typeVal="未知人员";
    }
    return typeVal;
}

