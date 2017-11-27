package controllers.user;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {

	@Before(priority = 100, unless = { "user.Application.index", "user.Application.version",
			"user.Application.qiniuUptoken", "user.Application.qiniuUptokenSimple", "user.PersonController.captcha",
			"user.PersonController.regist", "user.PersonController.login", "user.PersonController.forgetPassword" })
	public static void access() {
		accesstoken();
	}
}