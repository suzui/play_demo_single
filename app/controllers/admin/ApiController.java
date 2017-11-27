package controllers.admin;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {

	@Before(priority = 100, unless = { "admin.Application.index", "admin.Application.version",
			"admin.Application.qiniuUptoken", "admin.Application.qiniuUptokenSimple", "admin.AdminController.login" })
	public static void access() {
		accesstoken();
	}
}