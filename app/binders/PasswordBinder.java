package binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import play.data.binding.TypeBinder;
import utils.CodeUtils;

public class PasswordBinder implements TypeBinder<String> {

	@Override
	public String bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType)
			throws Exception {
		return value != null && value.length() < 32 ? CodeUtils.md5(value) : value;
	}
}