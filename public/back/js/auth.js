var auth_tab = function(){
	element.tabDelete('tab', nav_name);
	var auth_tab_html = $('#auth_tab').html(); 
	element.tabAdd('tab', {title : nav_name,content : auth_tab_html,id : nav_name});
	element.tabChange('tab', nav_name);
	auth_table(); 
}

var auth_table = function(param){
	$.post('/back/auth/list', param, function(result, status) {
		table.render({id: 'auth_table',elem: '#auth_table',data: result.data.array,page: true,limits: [10,50,100],limit:50,height: 'full-10',even:true,cols: [auth_table_title]});
	})
}

form.on('submit(auth_search)', function(data) {
	auth_table(data.field);
});

form.on('submit(auth_form)', function() {
	var d={id:'',name:'',codes:''};
	var auth_form_html = laytpl($('#auth_form').html()).render(d);
	layer_index = layer.open({
		type: 1,
		area: area_6_4,
		content: auth_form_html
	});
	form.render('checkbox');
});
form.on('submit(auth_add)',function(data){
	var param=data.field;
	var codes = [];
	var access = [];
	$.each($("input:checkbox[name='code']:checked"),function(index,item){
		codes.push($(item).val());
		access.push($(item).attr('title'));
	});
	param['codes']=codes.join(',');
	param['access']=access.join(',');
	$.post('/back/auth/add', param, function(result, status) {
		if(result.status=='succ'){
			auth_table();
			layer.close(layer_index);
		}else{
			layer.msg(result.message);
		}
	});
});

table.on('tool(auth_table)', function(obj){
	var e = obj.event;
	var d = obj.data;
	if(e === 'edit'){
		var auth_form_html = laytpl($('#auth_form').html()).render(d);
		layer_index = layer.open({
			type: 1,
			area: area_6_4,
			content: auth_form_html
		});
		form.render('checkbox');
		form.on('submit(auth_edit)',function(data){
			var param=data.field;
			var codes = [];
			var access = [];
			$.each($("input:checkbox[name='code']:checked"),function(index,item){
				codes.push($(item).val());
				access.push($(item).attr('title'));
			});
			param['codes']=codes.join(',');
			param['access']=access.join(',');
			$.post('/back/auth/edit', param, function(result, status) {
				if(result.status=='succ'){
					layer.close(layer_index);
					obj.update(param);
				}else{
					layer.msg(result.message);
				}
			});
		});
	} else if(e === 'del'){
		layer.confirm('确定删除角色', function(index){
			$.post('/back/auth/del', d, function(result, status) {
				if(result.status=='succ'){
					obj.del();
				}else{
					layer.msg(result.message);
				}
			});
			layer.close(index); 
		});
	}
});