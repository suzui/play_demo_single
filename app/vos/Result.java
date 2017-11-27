package vos;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import play.Logger;

/**
 * 网络请求结果
 *
 * @author Su
 *
 */
public class Result {

	public String status;

	public int code;

	public String message;

	public Data data;

	public long systemTime;

	public static class Status {
		public final static String SUCC = "succ";
		public final static String FAIL = "fail";
	}

	public static class StatusCode {
		public static final Object[] SUCCESS = { 20000, "请求成功" };
		public static final Object[] FAIL = { 50000, "系统异常" };
		public static final Object[] SYSTEM_TOKEN_UNVALID = { 40001, "accesstoken失效" };
		public static final Object[] SYSTEM_POST_REPEAT = { 40002, "post重复提交" };
		public static final Object[] SYSTEM_ACCESS_FOBIDDEN = { 40003, "无相应权限" };
		public static final Object[] SYSTEM_PARAM_ERROR = { 40004, "参数不合法" };
		public static final Object[] PERSON_USERNAME_UNVALID = { 40101, "用户名格式错误" };
		public static final Object[] PERSON_PHONE_UNVALID = { 40102, "手机号码格式错误" };
		public static final Object[] PERSON_EMAIL_UNVALID = { 40103, "邮箱格式错误" };
		public static final Object[] PERSON_PASSWORD_UNVALID = { 40104, "密码格式错误" };
		public static final Object[] PERSON_USERNAME_EXIST = { 40105, "该用户名已被使用" };
		public static final Object[] PERSON_PHONE_EXIST = { 40106, "该手机号码已被使用" };
		public static final Object[] PERSON_EMAIL_EXIST = { 40107, "该邮箱已被使用" };
		public static final Object[] PERSON_PASSWORD_ERROR = { 40108, "密码错误" };
		public static final Object[] PERSON_CAPTCHA_ERROR = { 40109, "验证码错误" };
		public static final Object[] PERSON_ACCOUNT_NOTEXIST = { 40110, "用户不存在" };
	}

	public Result() {
		systemTime = System.currentTimeMillis();
	}

	public static final ObjectMapper mapper = new ObjectMapper();
	static {
		// mapper.setSerializationInclusion(Include.NON_NULL);
	}

	public static String failed() {
		Result result = new Result();
		result.status = Status.FAIL;
		result.code = (int) StatusCode.FAIL[0];
		result.message = (String) StatusCode.FAIL[1];
		return convert(result);
	}

	public static String failed(Object[] codemessage) {
		Result result = new Result();
		result.status = Status.FAIL;
		result.code = (int) codemessage[0];
		result.message = (String) codemessage[1];
		return convert(result);
	}

	public static String succeed() {
		Result result = new Result();
		result.status = Status.SUCC;
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = (String) StatusCode.SUCCESS[1];
		return convert(result);
	}

	public static String succeed(String message) {
		Result result = new Result();
		result.status = Status.SUCC;
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = message;
		return convert(result);
	}

	public static String succeed(Data data) {
		Result result = new Result();
		result.status = Status.SUCC;
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = (String) StatusCode.SUCCESS[1];
		result.data = data;
		return convert(result);
	}

	public static String succeed(Data data, String message) {
		Result result = new Result();
		result.status = Status.SUCC;
		result.code = (int) StatusCode.SUCCESS[0];
		result.message = message;
		result.data = data;
		return convert(result);
	}

	public static String convert(Result result) {
		try {
			return mapper.writeValueAsString(result);
		} catch (IOException e) {
			Logger.info("[result failed]:%s", e.getMessage());
		}
		return null;
	}

}