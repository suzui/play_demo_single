var person_tab = function(){
	element.tabDelete('tab', nav_name);
	var person_tab_html = $('#person_tab').html(); 
	element.tabAdd('tab', {title : nav_name,content : person_tab_html,id : nav_name});
	element.tabChange('tab', nav_name);
	person_table();
}

var person_table = function(param){
	$.post('/back/person/list', param, function(result, status) {
		table.render({id: 'person_table',elem: '#person_table',data: result.data.array,page: true,limits: [10,50,100],limit:50,height: 'full-10',even:true,cols: [person_table_title]});
	})
}

form.on('submit(person_search)', function(data) {
	person_table(data.field);
});

form.on('submit(person_form)', function() {
	var d={id:'',name:'',username:'',phone:'',avatar:'',intro:''};
	var person_form_html = laytpl($('#person_form').html()).render(d);
	layer_index = layer.open({
		type: 1,
		area: area_8_6,
		content: person_form_html
	});
	image_upload('person_avatar');
	wang_editor('person_intro');
});
form.on('submit(person_add)',function(data){
	var param=data.field;
	param['intro']=editor.txt.html();
	$.post('/back/person/add', param, function(result, status) {
		if(result.status=='succ'){
			person_table();
			layer.close(layer_index);
		}else{
			layer.msg(result.message);
		}
	});
});

form.on('submit(person_list_del)',function(){
	var checkStatus = table.checkStatus('person_table');
	var ids=[];
	$.each(checkStatus.data,function (index, item) {
		ids.push(item.id);
    });
	layer.confirm('确定删除批量用户', function(index){
		$.post('/back/person/dels', {ids:ids.join(',')}, function(result, status) {
			if(result.status=='succ'){
				person_table();
			}else{
				layer.msg(result.message);
			}
			layer.close(index); 
		});
    });
});

table.on('tool(person_table)', function(obj){
	var e = obj.event;
	var d = obj.data;
	if(e === 'edit'){
		var person_form_html = laytpl($('#person_form').html()).render(d);
		layer_index = layer.open({
			type: 1,
			area: area_8_6,
			content: person_form_html
		});
		image_upload('person_avatar');
		wang_editor('person_intro');
		editor.txt.html(d.intro);
		form.on('submit(person_edit)',function(data){
			var param=data.field;
			param['intro']=editor.txt.html();
			$.post('/back/person/edit', param, function(result, status) {
				if(result.status=='succ'){
					layer.close(layer_index);
					obj.update(param);
				}else{
					layer.msg(result.message);
				}
			});
		});
	} else if(e === 'del'){
		layer.confirm('确定删除用户', function(index){
			$.post('/back/person/del', d, function(result, status) {
				if(result.status=='succ'){
					obj.del();
				}else{
					layer.msg(result.message);
				}
			});
			layer.close(index); 
		});
	} else if(e === 'auth'){
		$.post('/back/person/auths', {id:d.id}, function(result, status) {
			if(result.status=='succ'){
				d['data']=result.data.array;
				var person_auth_form_html = laytpl($('#person_auth_form').html()).render(d);
				layer_index = layer.open({
					type: 1,
					area: area_6_4,
					content: person_auth_form_html
				});
				form.render('checkbox');
				form.on('submit(person_auth)',function(data){
					var param=data.field;
					var auths=[];
					$.each($("input:checkbox[name='auth']:checked"),function(index,item){
						auths.push($(item).val());
					});
					param['authIds']=auths.join(',');
					$.post('/back/person/auth', param, function(result, status) {
						if(result.status=='succ'){
							layer.close(layer_index);
						}else{
							layer.msg(result.message);
						}
					});
				});
			}else{
				layer.msg(result.message);
			}
		});
	} else if(e === 'password'){
		var person_password_form_html = laytpl($('#person_password_form').html()).render(d);
		layer_index = layer.open({
			type: 1,
			content: person_password_form_html
		});
		form.on('submit(person_password)',function(data){
			var param=data.field;
			$.post('/back/person/password', param, function(result, status) {
				if(result.status=='succ'){
					layer.close(layer_index);
				}else{
					layer.msg(result.message);
				}
			});
		});
	}   
});