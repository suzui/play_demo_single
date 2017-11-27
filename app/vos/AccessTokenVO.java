package vos;

import annotations.DataField;

public class AccessTokenVO extends OneData {

	@DataField(name = "token")
	public String accesstoken;

	public AccessTokenVO() {
	}

	public AccessTokenVO(String accesstoken) {
		this.accesstoken = accesstoken;
	}

}
