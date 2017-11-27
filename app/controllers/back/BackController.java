package controllers.back;

import controllers.BaseController;
import models.person.Person;
import play.Logger;
import play.mvc.Before;
import play.mvc.Http.Request;
import vos.Result;
import vos.Result.StatusCode;

public class BackController extends BaseController {

	@Before(priority = 100, unless = { "back.Application.index", "back.Application.login" })
	static void access() {
		Logger.info("[access start]:================");
		Request request = Request.current();
		final Person person = getCurrPerson();
		if (person == null || !person.hasAccess()) {
			if (request.isAjax()) {
				renderJSON(Result.failed(StatusCode.SYSTEM_ACCESS_FOBIDDEN));
			}
			Application.index();
		}
		Logger.info("[access person]:%s", person.id);
		Logger.info("[access end]:================");
	}

}
