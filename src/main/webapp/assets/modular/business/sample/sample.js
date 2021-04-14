layui.use(['table', 'form', 'func', 'HttpRequest', 'util', 'upload'], function () {
    var $ = layui.$;
    var table = layui.table;
    var form = layui.form;
    var func = layui.func;
    var HttpRequest = layui.HttpRequest;
    var util = layui.util;
    var upload = layui.upload;
    var layer = layui.layer;


    // 样本管理
    var Sample = {
        tableId: "sampleTable"
    };

    // 初始化表格的列
    Sample.initColumn = function () {
        return [[
            {type: 'checkbox'},
            {field: 'sampleId', hide: true, title: '主键id'},
            {field: 'sampleZhName', sort: true, title: '样本名称'},
            {field: 'samplePath', sort: true, title: '存储位置'},           
            {field: 'sampleSuffix', sort: true, title: '样本后缀'},
            {field: 'sampleSizeKb', sort: true, title: '样本大小'},
            {
                field: 'createTime', sort: true, title: '创建时间', templet: function (d) {
                    return util.toDateString(d.createTime);
                }
            },
            {field: 'createUserName', sort: true, title: '创建人'},
            {align: 'center', toolbar: '#sampleBar', title: '操作', width: 230}
        ]];
    };

    //上传
/*   var uploadInst = upload.render({
        elem: '#btnUpload' //绑定元素
        , url: Feng.ctxPath + '/Sample/upload' //上传接口
        , done: function (res) {
            //上传完毕回调
            Feng.success("上传成功!");

            Sample.search();
        }
        , error: function (err) {
            //请求异常回调
            Feng.error("上传失败！" + err.message);
        }
    });  
*/

    // 点击查询按钮
    Sample.search = function () {
        var queryData = {};
        queryData['sampleZhName'] = $("#sampleZhName").val();
        queryData['sampleType'] = $("#sampleType").val();
        //queryData['positionCode'] = $("#positionCode").val();
        table.reload(Sample.tableId, {
            where: queryData,
            page: {curr: 1}
        });
    };


    // 点击详情
    Sample.openDetails = function (data) {
        func.open({
            title: '详情',
            content: Feng.ctxPath + '/view/sampleDetails?sampleId=' + data.sampleId,
            tableId: Sample.tableId
        });
    };


    // 点击删除
    Sample.onDeleteSample = function (data) {
        var operation = function () {
            var httpRequest = new HttpRequest(Feng.ctxPath + "/Sample/deleteReally", 'post', function (data) {
                Feng.success("删除成功!");
                table.reload(Sample.tableId);
            }, function (data) {
                Feng.error("删除失败!" + data.message + "!");
            });
            httpRequest.set(data);
            httpRequest.start(true);
        };
        Feng.confirm("是否删除?", operation);
    };


    // 下载
    Sample.onSampleDownload = function (data) {
        if (data.secretFlag === 'Y') {
            window.location.href = Feng.ctxPath + '/Sample/privateDownload?sampleId=' + data.sampleId;
        } else {
            window.location.href = Feng.ctxPath + '/Sample/publicDownload?sampleId=' + data.sampleId;
        }
    }


    // 渲染表格
    var tableResult = table.render({
        elem: '#' + Sample.tableId,
        url: Feng.ctxPath + '/Sample/sampleListPage',
        page: true,
        request: {pageName: 'pageNo', limitName: 'pageSize'}, //自定义分页参数
        height: "full-158",
        cellMinWidth: 100,
        cols: Sample.initColumn(),
        parseData: Feng.parseData
    });

    // 搜索按钮点击事件
    $('#btnSearch').click(function () {
        Sample.search();
    });


    // 工具条点击事件
    table.on('tool(' + Sample.tableId + ')', function (obj) {
        var data = obj.data;
        var event = obj.event;
        if (event === 'sampleDetails') {
            Sample.openDetails(data);
        } else if (event === 'delete') {
            Sample.onDeleteSample(data);
        } else if (event === 'download') {
            Sample.onSampleDownload(data);
        } else if (event === 'preview') {
            Sample.openPreview(data);
        }
    });

    // 修改状态
    form.on('switch(status)', function (obj) {
        var SampleId = obj.elem.value;
        var checked = obj.elem.checked ? 1 : 2;
        Sample.updateStatus(sampleId, checked);
    });
});
