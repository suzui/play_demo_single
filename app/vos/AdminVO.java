package vos;

import annotations.DataField;
import models.person.AccessToken;
import models.person.Person;

public class AdminVO extends OneData {

	@DataField(name = "管理员id", enable = false)
	public Long adminId;
	@DataField(name = "用户名")
	public String username;
	@DataField(name = "手机号")
	public String phone;
	@DataField(name = "姓名")
	public String name;
	@DataField(name = "头像")
	public String avatar;
	@DataField(name = "token", enable = false)
	public String accesstoken;

	public AdminVO() {

	}

	public AdminVO(Person person) {
		this.id = person.id;
		this.adminId = person.id;
		this.username = person.username;
		this.phone = person.phone;
		this.name = person.name;
		this.avatar = person.avatar;
	}

	public AdminVO(AccessToken accessToken) {
		this(accessToken.person);
		this.accesstoken = accessToken.accesstoken;
	}

}
