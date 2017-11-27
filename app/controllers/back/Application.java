package controllers.back;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import binders.PasswordBinder;
import enums.Access;
import models.auth.Auth;
import models.auth.AuthPerson;
import models.person.Person;
import play.data.binding.As;
import vos.Result;
import vos.Result.StatusCode;

public class Application extends BackController {

	public static void index() {
		final Person person = getCurrPerson();
		if (person != null) {
			home();
		}
		render();
	}

	public static void login(String username, @As(binder = PasswordBinder.class) String password) {
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
		setPersonIdToSession(person.id);
		renderJSON(Result.succeed());
	}

	public static void logout() {
		removePersonIdToSession();
		index();
	}

	public static void home() {
		final Person person = getCurrPerson();
		Set<String> access = new HashSet();
		if (person.isAdmin()) {
			access = Arrays.stream(Access.values()).map(a -> a.code() + "").collect(Collectors.toSet());
		} else {
			List<Auth> auths = AuthPerson.fetchAuthByPerson(person);
			access = auths.stream().flatMap(a -> Arrays.stream(StringUtils.split(a.codes, ",")))
					.collect(Collectors.toSet());
		}
		render(person, access);
	}
}