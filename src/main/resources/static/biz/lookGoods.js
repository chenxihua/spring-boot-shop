
//  ----------------------      分割     ----------------------

layui.use([ 'layer', 'form', 'element', 'laypage' ], function() {
    var layer = layui.layer;
    var element = layui.element;
    var form = layui.form;
    var laypage = layui.laypage;
    var $ = layui.$;

    loadSelectedInput();

    // 重新渲染表单
    form.render(null,'loadSelected');

    var currPage = 1;
    var limit = 12;

    var keyword = $('#keyword').val();
    var typeValue = $('#schoolSelected').val();
    var itemsVal = $('#typeSelected').val();

    haha(currPage, limit, typeValue, itemsVal, keyword);

});

var loadSelectedInput = function () {
    $.ajax({
        url: '/items/getSelectedDates',
        type: 'post',
        dataType: 'json',
        async:false,
        success: function (data) {
            console.log("code: "+data.code);
            if (data.code==0){
                var htmlItem = "";
                var htmlSch = "";

                $("#schoolSelected").html("");
                $("#typeSelected").html("");

                var publishSchs = data.schoolDatas;
                htmlSch += "<option value=''>直接选择或者搜索</option>";
                var lens = publishSchs.length;
                for (i=0; i<lens; i++) {
                    htmlSch += "<option value='"+publishSchs[i].id+"'>"+publishSchs[i].fullName+"</option>";
                }

                var goodTypes = data.itemDatas;
                htmlItem += "<option value=''>直接选择或者搜索</option>";
                var lens = goodTypes.length;
                for (i=0; i<lens; i++) {
                    htmlItem += "<option value='"+goodTypes[i].id+"'>"+goodTypes[i].name+"</option>";
                }

                $("#typeSelected").html(htmlItem);
                $("#schoolSelected").html(htmlSch);



            }else {
                layer.alert("加载数据失败，无法响应");
            }
        },
        error: function () {
            layer.alert("无法加载下拉框数据，服务器出现异常。")
        }
    });
}

var reQuery = function () {
    var currPage = 1;
    var limit = 12;

    var keyword = $('#keyword').val();
    var typeValue = $('#schoolSelected').val();
    var itemsVal = $('#typeSelected').val();
    haha(currPage, limit, typeValue, itemsVal, keyword);
}

var haha = function (currPage, limit, typeValue, itemsVal, keyword) {

    $.ajax({
        url: '/goods/searchBuyList',
        type: 'POST',
        data: {'schoolId': typeValue, 'itVal':itemsVal, 'pageIndex':currPage, 'pageLimit':limit, 'keyword': keyword},
        dataType: 'json',
        success: function (result) {
            if (result.code==0){
                if (result.data==''){
                    parent.layer.alert("此次查询没有数据！")
                } else {
                    paged(result, typeValue, itemsVal, keyword);
                }
            }else {
                parent.layer.alert("出错了");
            }
        },
        error: function () {
            parent.layer.alert("服务器出错了，请重新检查");
        }
    });
}

var paged = function (data, typeVal, itemsVal, keyword) {
    layui.use(['laypage', 'layer'], function(){
        var laypage = layui.laypage;
        var layer = layui.layer;
        var $ = layui.$;

        laypage.render({
            elem: 'testExample',
            count: data.totalCounts, //数据总数
            curr:  data.number+1,
            limit: data.size,
            layout: ['count', 'prev', 'page', 'next',  'refresh', 'skip'],
            jump: function (obj, first) {
                // console.log(obj.curr); //得到当前页，以便向服务端请求对应页的数据。
                // console.log(obj.limit); //得到每页显示的条数
                // 首次不执行
                if(!first){
                    // var typeValue = $('#schoolSelect').val();
                    // var itemsVal = $('#itemsId').val();
                    haha(obj.curr, obj.limit, typeVal, itemsVal, keyword);  //加载数据
                }
            }
        });

        // 渲染页面视图函数
        pageViews(data);
    });
}

var pageViews = function (data) {
    var html = "";
    $('#photoSection').html("");
    var goodsData = data.data;
    var lens = goodsData.length;
    for(i=0; i<lens; i++){
        var paraUrl = goodsData[i].id;
        html += "<li><div class='col-md-6' id='imgLayer'><a href='/common/getGoodInfo/"+paraUrl+"'><img src='"+goodsData[i].oneAddress+"' class='steal' /></a></div><p>"+goodsData[i].name+"</p></li>";
    }
    $('#photoSection').html(html);
}

