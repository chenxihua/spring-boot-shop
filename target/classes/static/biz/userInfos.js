
layui.use([ 'layer', 'form', 'element', 'util' ], function() {
    var layer = layui.layer;
    var form = layui.form;
    var element = layui.element;
    var util = layui.util;
    var $ = layui.$;

    var userId;

    showUserInfos();

    function showUserInfos(){
        $.ajax({
            url:'/user/findOne',
            type:'post',
            // async:'false',
            dataType:'json',
            success: function (data) {
                if (data.success){
                    // 将查询出来的数据，赋值给userId；
                    userId = data.data.id;

                    form.val('example', {
                        "id":data.data.id,
                        "account":data.data.account,
                        "realName":data.data.realName,
                        "schoolId":data.schoolName,
                        "signTime":util.toDateString(data.data.signTime, 'yyyy-MM-dd HH:mm:ss'),
                        "email":data.data.email,
                        "phone":data.data.phone,
                        "wechat":data.data.wechat,
                        "qq":data.data.qq,
                        "status":statusString(data.data.status),
                        "type":typeString(data.data.type),
                        "signature":data.data.signature,
                        "address":data.data.address,
                    });
                }
                form.render();
            }
        });
    }

    // 更新个人信息
    $('#update_btn').click(function () {
        layer.open({
            type:2,
            title: '<p style="text-align: center;">更改个人非敏感信息</p>',
            closeBtn: 1,
            area: ['600px', '400px'],
            shadeClose: false,
            content: '/user/updateInfo/'+userId,
            end:function () {
                showUserInfos();
            }
        });
    });

    // 查看个人信用
    $('#credit_btn').click(function () {
        var publishMan = userId;
        layer.open({
            type:2,
            title: '<p style="color: #009688; font-size: 20px; text-align: center;">查看个人信用</p>',
            closeBtn: 1,
            area: ['520px', '310px'],
            shadeClose: false,
            content: '/userCredit/show_credit/'+publishMan,
        });
    });

    // 将状态显示成文字(用户状态)
    function statusString(statusId){
        var statusName = '';
        if (statusId==1){
            statusName="未认证";
        } else if(statusId==2){
            statusName="完成认证";
        }else{
            statusName="未知状态";
        }
        return statusName;
    }

    // 将人员类型显示成文字
    function typeString(typeId){
        var typeName = '';
        if (typeId==1) {
            typeName="学生";
        }else if (typeId==2){
            typeName="老师";
        } else if(typeId==3){
            typeName="其他人员";
        }else if(typeId==4){
            typeName="系统管理员";
        }else{
            typeName="未知人员";
        }
        return typeName;
    }

});

