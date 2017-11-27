package binders;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.Gson;

import play.Logger;
import play.data.binding.Global;
import play.data.binding.TypeBinder;
import play.i18n.Lang;
import play.mvc.Http.Request;
import vos.OneData;

import static java.lang.System.err;

@Global
public class DataBinder implements TypeBinder<OneData> {
    public static final Gson GSON = new Gson();
    
    @Override
    public Object bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) {
        Map<String, Object> params = new HashMap<>();
        String lang = Lang.get();
        for (Entry<String, String> e : Request.current().params.allSimple().entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            if (v == null || v.equals("null") || v.equals("undefined")) {
                continue;
            }
            if (v.equals("")) {
                Field field = null;
                try {
                    field = actualClass.getField(k);
                } catch (NoSuchFieldException nsfe) {
                    continue;
                }
                if (!String.class.isAssignableFrom((Class<?>) field.getGenericType())) {
                    continue;
                }
            }
            if (v.startsWith("[") && v.endsWith("]")) {
                params.put(k, GSON.fromJson(v, List.class));
            } else if (!"vo,body".contains(k)) {
                char[] cs = k.toCharArray();
                cs[0] -= 32;
                params.put(lang + String.valueOf(cs), v);
                params.put(k, v);
            }
        }
        if (!params.containsKey("page"))
            params.put("page", 1);
        if (!params.containsKey("size"))
            params.put("size", Integer.MAX_VALUE);
        String json = GSON.toJson(params);
        Logger.info("[databinder]:%s", json);
        return GSON.fromJson(json, actualClass);
    }
}