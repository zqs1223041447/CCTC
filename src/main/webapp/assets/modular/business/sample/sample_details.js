layui.use(['layer', 'form', 'admin', 'HttpRequest', 'upload'], function () {
    var $ = layui.$;
    var form = layui.form;
    var HttpRequest = layui.HttpRequest;
	var upload = layui.upload;
    //获取详情信息，填充表单
    var httpRequest = new HttpRequest(Feng.ctxPath + "/Sample/detail?sampleId=" + Feng.getUrlParam("sampleId"),'get');
    var result = httpRequest.start();
    form.val("sampleForm", result.data);
 

/*
	var uploadInst = update.render({
			elem: '#btnUpload' //绑定元素
			, url: Feng.ctxPath + '/Sample/update' //上传接口
			, done: function (res) {
				//上传完毕回调
				Feng.success("更新成功!");

				Sample.search();
			}
			, error: function (err) {
				//请求异常回调
				Feng.error("更新失败！" + err.message);
			}
		});
		
		*/
		  //选完文件后不自动上传
	var update = upload.render({
		elem: '#selectFile'
		,url: Feng.ctxPath + '/Sample/update'        //改成您自己的上传接口
		,auto: false
		//,multiple: true
		,bindAction: '#hideUpload'
		,before: function(obj){ //obj参数包含的信息，跟 choose回调完全一致，可参见上文。
			 this.data = {
					 sampleId: $('#sampleId').val(),
					 sampleType: $('#sampleType').val(),
					 sampleZhName: $('#sampleZhName').val(),
					 sampleEnName: $('#sampleEnName').val(),
					 sampleIntroduction: $('#sampleIntroduction').val()
			 };
		}
		,done: function(res){
		 //上传完毕回调
				Feng.success("更新成功!");
				
		}
		, error: function (err) {
				//请求异常回调
				Feng.error("更新失败！" + err.message);
		}
	  });	
	
	form.on('submit(submit111)', function(data){
		 console.log(data.field) 
		 var formData = new FormData($( "#sampleForm" )[0]);  
		 console.info(formData);
		 //parent.layer.msg("2");
		 var saveLoading = parent.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});
		 $.ajax({
               url : Feng.ctxPath + '/Sample/update',
               type : 'post',
               async: false,
               data : formData,
               cache: false,
               contentType: false,  
               processData: false, 
               success : function(data) {
               	parent.layer.close(saveLoading);
               	
               	Feng.success("更新成功!");
               	
               }
	  		});
	});
});
