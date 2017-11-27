package models.person;

import java.util.List;

import javax.persistence.Entity;

import utils.CodeUtils;

@Entity
public class Admin extends Person {

	public static Admin add(String username, String password) {
		Admin admin = new Admin();
		admin.username = username;
		admin.password = CodeUtils.md5(password);
		return admin.save();
	}

	public static void init() {
		List<Admin> admins = Admin.find(defaultSql()).fetch();
		if (admins.isEmpty()) {
			add("admin", "123456");
		}
	}

}
