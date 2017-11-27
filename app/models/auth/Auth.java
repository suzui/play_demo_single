package models.auth;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;

import org.apache.commons.lang.StringUtils;

import enums.Access;
import models.BaseModel;
import vos.AuthVO;

@Entity
public class Auth extends BaseModel {

	public String name;
	public String codes;

	public static Auth add(AuthVO authVO) {
		Auth auth = new Auth();
		auth.edit(authVO);
		return auth;
	}

	public void edit(AuthVO authVO) {
		this.name = authVO.name != null ? authVO.name : name;
		this.codes = authVO.codes != null ? authVO.codes : codes;
		this.save();
	}

	public String access() {
		return StringUtils.join(Arrays.stream(StringUtils.split(this.codes, ","))
				.map(code -> Access.covert(Integer.parseInt(code)).name()).collect(Collectors.toList()), ",");
	}

	public void del() {
		this.logicDelete();
		AuthPerson.delByAuth(this);
	}

	public static Auth findByID(Long id) {
		return Auth.find(defaultSql("id =?"), id).first();
	}

	public static List<Auth> fetchByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty()) {
			return Collections.EMPTY_LIST;
		}
		return Auth.find(defaultSql("id in(:ids)")).bind("ids", ids.toArray()).fetch();
	}

	public static List<Auth> fetchAll() {
		return Auth.find(defaultSql()).fetch();
	}

	public static List<Auth> fetch(AuthVO authVO) {
		Object[] data = data(authVO);
		List<String> hqls = (List<String>) data[0];
		List<Object> params = (List<Object>) data[1];
		return Auth.find(defaultSql(StringUtils.join(hqls, " and ")) + authVO.condition(), params.toArray())
				.fetch(authVO.page, authVO.size);
	}

	public static int count(AuthVO authVO) {
		Object[] data = data(authVO);
		List<String> hqls = (List<String>) data[0];
		List<Object> params = (List<Object>) data[1];
		return (int) Auth.count(defaultSql(StringUtils.join(hqls, " and ")), params.toArray());
	}

	private static Object[] data(AuthVO authVO) {
		List<String> hqls = new ArrayList<>();
		List<Object> params = new ArrayList<>();
		if (StringUtils.isNotBlank(authVO.search)) {
			hqls.add("name like ?");
			params.add("%" + authVO.search + "%");
		}
		return new Object[] { hqls, params };
	}
}