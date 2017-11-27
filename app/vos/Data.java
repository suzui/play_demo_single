package vos;

//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@class")
public class Data {

	public Data() {

	}

	// 序列化
	// @JsonProperty("") 序列化对应字段
	// @JsonInclude(Include.NON_NULL) 序列化不输出

	// 接收参数文档规则
	// @DataField(name = "",demo="",enable=false)
	// name:参数说明
	// demo:参数举例
	// enable false则默认不在接受参数列表(Id结尾不受限制)(param、except不受限制)
	// param 从自身及父类定义中取值 except从自身取值

	// 输出成员文档规则
	// 仅输出自身成员 与父类中字段重复 transient 单独申明
	// @DataField(name = "") name:参数说明
	// @JsonInclude(Include.NON_NULL) 不输出字段

}
