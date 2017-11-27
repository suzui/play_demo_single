package controllers.admin;

import annotations.ActionMethod;
import annotations.ParamField;
import binders.PasswordBinder;
import models.person.AccessToken;
import models.person.Admin;
import models.person.Person;
import play.data.binding.As;
import vos.AdminVO;
import vos.Result;
import vos.Result.StatusCode;

public class AdminController extends ApiController {
    
    @ActionMethod(name = "管理员登录", clazz = AdminVO.class)
    public static void login(@ParamField(name = "用户名") String username,
                             @ParamField(name = "密码") @As(binder = PasswordBinder.class) String password) {
        Person person = Person.findByUsername(username);
        if (person == null) {
            renderJSON(Result.failed(StatusCode.PERSON_ACCOUNT_NOTEXIST));
        }
        if (!person.isPasswordRight(password)) {
            renderJSON(Result.failed(StatusCode.PERSON_PASSWORD_ERROR));
        }
        if (!person.hasAccess()) {
            renderJSON(Result.failed(StatusCode.SYSTEM_ACCESS_FOBIDDEN));
        }
        AccessToken accessToken = AccessToken.add(person);
        renderJSON(Result.succeed(new AdminVO(accessToken)));
    }
    
    @ActionMethod(name = "管理员登出")
    public static void logout() {
        renderJSON(Result.succeed());
    }
    
    @ActionMethod(name = "管理员详情", clazz = AdminVO.class)
    public static void info() {
        Person person = getPersonByToken();
        AccessToken accessToken = AccessToken.findByPerson(person);
        renderJSON(Result.succeed(new AdminVO(accessToken)));
    }
    
}