package controllers.mobile;

import controllers.BaseController;
import play.mvc.Before;

public class ApiController extends BaseController {
    
    @Before(priority = 100, unless = {"mobile.Application.index"})
    public static void access() {
        accesstoken();
    }
}
