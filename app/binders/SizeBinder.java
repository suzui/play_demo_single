package binders;

import play.data.binding.TypeBinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class SizeBinder implements TypeBinder<Integer> {

	@Override
	public Integer bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType)
			throws Exception {
		return value == null ? Integer.MAX_VALUE : Integer.parseInt(value);
	}
}