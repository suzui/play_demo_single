package models.auth;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import models.BaseModel;
import models.person.Person;

@Entity
public class AuthPerson extends BaseModel {

	@ManyToOne
	public Auth auth;
	@ManyToOne
	public Person person;

	public static AuthPerson add(Auth auth, Person person) {
		AuthPerson authPerson = findByAuthAndPerson(auth, person);
		if (authPerson != null) {
			return authPerson;
		}
		authPerson = new AuthPerson();
		authPerson.auth = auth;
		authPerson.person = person;
		return authPerson.save();
	}

	public void del() {
		this.logicDelete();
	}

	public static void delByAuth(Auth auth) {
		fetchByAuth(auth).forEach(ap -> ap.del());
	}

	public static void delByPerson(Person person) {
		fetchByPerson(person).forEach(ap -> ap.del());
	}

	public static AuthPerson findByAuthAndPerson(Auth auth, Person person) {
		return AuthPerson.find(defaultSql("auth=? and person=?"), auth, person).first();
	}

	public static List<AuthPerson> fetchByAuth(Auth auth) {
		return AuthPerson.find(defaultSql("auth=?"), auth).fetch();
	}

	public static List<AuthPerson> fetchByPerson(Person person) {
		return AuthPerson.find(defaultSql("person=?"), person).fetch();
	}

	public static List<Auth> fetchAuthByPerson(Person person) {
		return AuthPerson.find(defaultSql("select ap.auth from AuthPerson ap where ap.person=?"), person).fetch();
	}

}
