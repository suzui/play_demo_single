var $ = layui.jquery
var element = layui.element;
var form = layui.form;
var laytpl = layui.laytpl;
var tree=layui.tree;
var table = layui.table;
var layer = layui.layer;
var upload = layui.upload;
var carousel = layui.carousel;
var E = window.wangEditor;

var area_8_6 = ['800px','600px'],area_7_6 = ['700px','600px'],area_6_6 = ['600px','600px'],
	area_8_5 = ['800px','500px'],area_7_5 = ['700px','500px'],area_6_5 = ['600px','500px'],
	area_8_4 = ['800px','400px'],area_7_4 = ['700px','400px'],area_6_4 = ['600px','400px'];

var access = [];
access.push({name:'超级管理员',code:100});
access.push({name:'权限管理',code:101});
access.push({name:'统计管理',code:102});
access.push({name:'日志管理',code:103});
access.push({name:'系统设置',code:104});
access.push({name:'用户管理',code:105});

var layer_index=null;
var nav_name=null;
var nav_type=null;
var editor=null;

element.on('nav(nav)', function(elem) {
	nav_name=elem.text();
	nav_type=elem.attr("data");
	if(nav_type=='auth'){
		auth_tab();
	}else if(nav_type=='person'){
		person_tab();
	}
});

var image_upload = function (upload_id){
	upload.render({
		elem: '#'+upload_id,
		url: 'http://oss.iclass.cn/formImage',
		field:'qqfile',
		data: {bucketName:'smallfiles',source:'WEB'},
	    done: function(result){
	    		 $('#'+upload_id+'_img').attr('src',result.html);
	    		 $('#'+upload_id+'_input').val(result.html);
	    }
	});	 
}

var wang_editor = function(editor_id){
	editor = new E('#'+editor_id);
	editor.customConfig.uploadImgMaxSize = 3 * 1000 * 1000;
	editor.customConfig.customAlert = function (info) {
		layer.msg(info);
	}
	editor.customConfig.customUploadImg = function (files, insert) { 
		var formData = new FormData();
		formData.append('qqfile',files[0]); 
		formData.append('bucketName','smallfiles'); 
		formData.append('source','WEB');
		$.ajax({
			url: 'http://oss.iclass.cn/formImage' ,
			type: 'POST',
			data: formData,
			async: false,
			contentType: false,
			processData: false,
			success: function (result) {
				insert($.parseJSON(result).html);
			},
			error: function (result) {
				    
			}
		});
	}
	editor.create();
}