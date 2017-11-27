package vos;

import annotations.DataField;

public class QiniuVO extends OneData {

	@DataField(name = "七牛凭证")
	public String uptoken;

	public QiniuVO() {
	}

	public QiniuVO(String uptoken) {
		this.uptoken = uptoken;
	}

}
