var $ = layui.jquery
var element = layui.element;
var form = layui.form;
var layer = layui.layer;

form.on('submit(login)', function(data) {
	$.post('/back/login', data.field, function(result, status) {
		if(result.status=='succ'){
			location.href = "/back/home";
		}else{
			layer.msg(result.message);
		}
	});
	return false;
});