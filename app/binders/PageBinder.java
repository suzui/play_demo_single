package binders;

import play.data.binding.TypeBinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class PageBinder implements TypeBinder<String> {

	@Override
	public Integer bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType)
			throws Exception {
		return value == null ? 1 : Integer.parseInt(value);
	}
}