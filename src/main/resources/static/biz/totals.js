var table;
var layer;
var util;
var element;
var $;




layui.use(['layer',  'element'], function(){
    layer = layui.layer;
    element = layui.element;
    $ = layui.$;

    loadMaxDate();
    loadSexPartion();
    loadSexPublish();
    loadPartionsRatio();


});

// 加载首先显示的东西
var loadMaxDate = function () {
    $.ajax({
        url: '/admins/show_max_datas',
        dataType: 'json',
        type: 'post',
        success: function (data) {
            if (data.code==0){
                $('#max_p_user').text(data.maxPublishUser);
                $('#max_b_user').text(data.maxTradeUser);
                $('#max_p_sch').text(data.maxPubSchool);
                $('#max_b_sch').text(data.maxTradeSchool);
            }else {
                parent.layer.alert("查询失败，出现未知错误");
            }
        },
        error: function () {
            parent.layer.alert("服务器出错了，请检查服务器再操作！");
        }
    });
}



// 加载性别购买占比
var loadSexPartion = function () {
    var myChart = echarts.init(document.getElementById('sex_pie'));
    $.ajax({
        url: '/admins/show_sex_ratio',
        dataType: 'json',
        type: 'post',
        success: function (data) {
            if (data.code==0){
                myChart.setOption({
                    title : {
                        text: '男女生购买二手商品占比',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['男性', '女性']
                    },
                    series : [
                        {
                            name: '访问来源',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:[
                                {value:data.manNum, name:'男性'},
                                {value:data.womanNum, name:'女性'},

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
            }
        },
        error:function () {
            // showLoading遮盖层显示
            myChart.showLoading({
                text: '数据正在努力加载...',
                textStyle: { fontSize : 30 , color: '#444' },
                effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
            });
        }
    });
}

// 加载性别发布比例
var loadSexPublish = function () {
    var myChart = echarts.init(document.getElementById('sex_bar'));
    $.ajax({
        url: '/admins/show_sex_publish',
        dataType: 'json',
        type: 'post',
        success: function (data) {
            if (data.code==0){
                myChart.setOption({
                    title : {
                        text: '男女生发布二手商品占比',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['男性', '女性']
                    },
                    series : [
                        {
                            name: '访问来源',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:[
                                {value:data.manPub, name:'男性'},
                                {value:data.womanPub, name:'女性'},

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
            }
        },
        error:function () {
            // showLoading遮盖层显示
            myChart.showLoading({
                text: '数据正在努力加载...',
                textStyle: { fontSize : 30 , color: '#444' },
                effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
            });
        }
    });
}


// 加载显示地区发布比例
var loadPartionsRatio = function () {
    var myChartCn = echarts.init(document.getElementById('partion_pie'));
    $.ajax({
        url: '/admins/show_cn_area',
        dataType: 'json',
        type: 'post',
        success: function (data) {
            if (data.code==0){
                var datasList = data.data;
                myChartCn.setOption({
                    title : {
                        text: '全国分区发布量占比饼图',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{a} <br/>{b} : {c} ({d}%)"
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: [datasList[0].key, datasList[1].key,datasList[2].key,datasList[3].key,datasList[4].key,datasList[5].key,datasList[6].key,datasList[7].key]
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
                                {value:datasList[6].numData, name:datasList[6].key},
                                {value:datasList[7].numData, name:datasList[7].key},
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
            }
        },
        error:function () {
            // showLoading遮盖层显示
            myChartCn.showLoading({
                text: '数据正在努力加载...',
                textStyle: { fontSize : 30 , color: '#444' },
                effectOption: {backgroundColor: 'rgba(0, 0, 0, 0)'}
            });
        }
    });
}


