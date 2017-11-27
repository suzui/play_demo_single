package controllers.back;

import java.util.List;
import java.util.stream.Collectors;

import binders.PasswordBinder;
import models.auth.Auth;
import models.auth.AuthPerson;
import models.person.Person;
import play.data.binding.As;
import vos.AuthVO;
import vos.PageData;
import vos.PersonVO;
import vos.Result;

public class PersonController extends BackController {

	public static void list(PersonVO vo) {
		List<Person> persons = Person.fetch(vo);
		List<PersonVO> personVOs = persons.stream().map(p -> new PersonVO(p)).collect(Collectors.toList());
		renderJSON(Result.succeed(new PageData(personVOs)));
	}

	public static void add(PersonVO vo) {
		Person.add(vo);
		renderJSON(Result.succeed());
	}

	public static void edit(PersonVO vo) {
		Person person = Person.findByID(vo.id);
		person.edit(vo);
		renderJSON(Result.succeed());
	}

	public static void del(long id) {
		Person person = Person.findByID(id);
		person.del();
		renderJSON(Result.succeed());
	}

	public static void dels(@As(",") List<Long> ids) {
		List<Person> persons = Person.fetchByIds(ids);
		persons.forEach(p -> p.del());
		renderJSON(Result.succeed());
	}

	public static void auths(long id) {
		Person person = Person.findByID(id);
		List<Auth> auths = Auth.fetchAll();
		List<Auth> personAuths = AuthPerson.fetchAuthByPerson(person);
		List<AuthVO> authVOs = auths.stream().map(a -> new AuthVO(a, personAuths.contains(a)))
				.collect(Collectors.toList());
		renderJSON(Result.succeed(new PageData(authVOs)));
	}

	public static void auth(long id, @As(",") List<Long> authIds) {
		Person person = Person.findByID(id);
		List<Auth> auths = Auth.fetchByIds(authIds);
		auths.forEach(a -> AuthPerson.add(a, person));
		renderJSON(Result.succeed());
	}

	public static void password(long id, @As(binder = PasswordBinder.class) String password) {
		Person person = Person.findByID(id);
		person.editPassword(password);
		renderJSON(Result.succeed());
	}

}
