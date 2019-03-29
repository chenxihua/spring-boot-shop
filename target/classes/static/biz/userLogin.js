var table;
var layer;
var carousel;
var util;
var element;
var $;

layui.use(['layer','carousel', 'table', 'element'], function(){
    layer = layui.layer;
    carousel = layui.carousel;
    table = layui.table;
    element = layui.element;
    $ = layui.$;


    // 前台用户方法
    loadCarousels();

    loadDate();
    // 第二部分
    loadAdminPic();
    loadAdminBar();




    table.render({
        elem: '#category',
        height: 450,
        url: '/notices/notices_view',
        page: true, // 开启分页
        limits: [10, 20],
        limit: 10,
        cols: [
            [ // 表头
                {
                    field: 'title',
                    title: '公告标题',
                    // width: 200,
                    align: 'center'
                }, {
                    field: 'userId',
                    title: '公告发布人',
                    // width: 400,
                    align: 'center',
                    templet:function(d){ return idToString(d.userId); }
                }, {
                    field: 'contents',
                    title: '公告内容',
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

    function idToString(id){
        var userName = '';
        $.ajax({
            url:'/admins/findOne/'+id,
            type:'POST',
            async:false,
            dataType:'json',
            success:function (data) {
                userName = data.data.realName;
            }
        });
        return userName;
    }

    // 监听工具条时间
    table.on('tool(tools)', function (obj) {
        var data = obj.data;
        var layEvent = obj.event;
        var id = data.id;
        if (layEvent === 'look_Btn') {
            updataDialog(id);
        }
    });
});

var updataDialog = function (id) {
    var url = '/notices/look_notices/'+id;
    layer.open({
        type: 2
        , title: "公告信息"
        , area: ['600px', '350px']
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
}

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
// 将用户id，转为用户名字


// 第二部分
var loadDate = function(){
    $.post('/admins/sys_view', function(data){
        if(data.code==0){
            $('#sysCounts').text(data.userCounts);
            $('#sysRegister').text(data.todayCounts);
            $('#sysPublishs').text(data.goodCounts);
            $('#sysBuys').text(data.buyCounts);
        }else {
            parent.layer.alert("服务器出错了，请检查服务器再操作！");
        }
    });
}

var loadAdminPic = function () {
    var myChart = echarts.init(document.getElementById('larry-seo-stats'));
    $.ajax({
        url:'/admins/good_different',
        type:'post',
        dataType:'json',
        success:function (data) {
            if (data.code==0){
                var datasList = data.data;
                myChart.setOption({
                    title : {
                        text: '发布二手商品类型占比',
                        // subtext: '纯属虚构',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: [datasList[0].key,datasList[1].key,datasList[2].key,datasList[3].key,datasList[4].key,datasList[5].key,datasList[6].key,]
                    },
                    series : [
                        {
                            name: '访问来源',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:[
                                {value:datasList[0].numData, name:datasList[0].key},
                                {value:datasList[1].numData, name:datasList[1].key},
                                {value:datasList[2].numData, name:datasList[2].key},
                                {value:datasList[3].numData, name:datasList[3].key},
                                {value:datasList[4].numData, name:datasList[4].key},
                                {value:datasList[5].numData, name:datasList[5].key},
                                {value:datasList[6].numData, name:datasList[6].key}
                            ],
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                });
            }else {
                parent.layer.alert("发生异常，请检查一下服务器。");
            }
        },
        error: function () {
            // showLoading遮盖层显示
            myChart.showLoading({
                text: '数据正在努力加载...',
                textStyle: { fontSize : 30 , color: '#444' },
                effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
            });
        }
    });
}

var loadAdminBar = function () {
    var myPic = echarts.init(document.getElementById('larry-pic-btn'));
    $.ajax({
        url: '/admins/shop_status',
        type: 'post',
        dataType: 'json',
        success: function (data) {
            if(data.success){
                var datasList = data.data;
                myPic.setOption({
                    title: {
                        text: '系统二手商品状态数量'
                    },
                    tooltip: {},
                    legend: {
                        data:['数量(单位/个)']
                    },
                    xAxis: {
                        data: ["审核失败","等待审核","正在出售","下订状态","商品下架"]
                    },
                    yAxis: {},
                    series: [{
                        name: '数量(单位/个)',
                        type: 'bar',
                        data: [datasList[0].numData,datasList[1].numData,datasList[2].numData,datasList[3].numData,datasList[4].numData ]
                    }]
                });
            }else {
                parent.layer.alert("系统出错, 无法加载");
            }
        },
        error: function () {
            // showLoading遮盖层显示
            myPic.showLoading({
                text: '数据正在努力加载...',
                textStyle: { fontSize : 30 , color: '#444' },
                effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
            });
        }
    });
}

var loadCarousels = function () {
    layui.use('carousel', function () {
        var carousel = layui.carousel;
        //***************************建造实例
        var ins=carousel.render({
            elem: '#test1'
            , width: '600px'     //设置容器宽度
            , height: '400px'
            , arrow: 'always'    //始终显示箭头，可选hover,none
            //,anim: 'updown'    //切换动画方式，可选fade,default
            , full: false        //全屏
            , autoplay: true     //自动切换
            , interval: 3000     //自动切换的时间间隔
            , index: 0           //初始化时item索引,默认时0
            , indicator:'inside' //指示器位置，可选outside,none
        });

        //**************************监听轮播切换事件
        carousel.on('change(carofilter)', function (obj) { //test1来源于对应HTML容器的 lay-filter="test1" 属性值
            // console.log(obj.index);     //当前条目的索引
            // console.log(obj.prevIndex); //上一个条目的索引
            // console.log(obj.item);      //当前条目的元素对象
        });

        //****************************重置轮播
        //var ins = carousel.render(options);
        ins.reload({arrow:'hover'});//将arror从alway变成hover
    });
}

// 跳出系统提示框
var jumpTips = function () {
    //示范一个公告层
    layer.open({
        type: 1
        ,title: false //不显示标题栏
        ,closeBtn: false
        ,area: '300px;'
        ,shade: 0.8
        ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
        ,btn: ['确认退出提示']
        ,btnAlign: 'c'
        ,moveType: 1 //拖拽模式，0或者1
        ,content: '<div style="background-color: white; color: #009688;"><p style="text-align: left; font-size: 20px; font-family: 楷体;">1: 这里，你可以直接在校园内发布，购买二手商品。</p>'
        + '<p style="text-align: left; font-size: 20px; font-family: 楷体;">2: 在平台系统内发布二手商品，均需要管理员审核商品信息，才能发布到平台内进行出售。</p>'
        + '<p style="text-align: left; font-size: 20px; font-family: 楷体;">3: 在平台内所购买的二手商品，均已经通过管理员审核，可以放心购买。</p>'
        + '<p style="text-align: left; font-size: 20px; font-family: 楷体;">4: 如遇到卖家出售假货，违禁品，均可向平台举报，我们会第一时间处理你的请求。</p>'
        + '<p style="text-align: left; font-size: 20px; font-family: 楷体;">5: 如需维权，请联系我们客服电话：010-110</p></div>'
        ,end: function () {
            layer.closeAll();
        }
    });
}