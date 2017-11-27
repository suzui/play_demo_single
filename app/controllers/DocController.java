package controllers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.*;

import annotations.EnumClass;
import enums.Access;
import interfaces.BaseEnum;
import org.apache.commons.lang.StringUtils;

import com.google.gson.Gson;

import annotations.ActionMethod;
import annotations.DataField;
import annotations.ParamField;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import play.Play;
import play.mvc.Before;
import play.mvc.Controller;
import utils.JSONUtils;
import vos.Data;
import vos.OneData;
import vos.PageData;
import vos.Result;

public class DocController extends Controller {
    private static final String ID = "Id";
    private static final String DOC = "doc";
    
    @Before(priority = -1)
    static void doc() {
        if (!request.params._contains(DOC)) {
            return;
        }
        try {
            String codes = "";
            Class<Result.StatusCode> statusCode = Result.StatusCode.class;
            for (Field field : statusCode.getDeclaredFields()) {
                Object[] value = (Object[]) field.get(statusCode);
                codes += (value[0] + " " + value[1] + "\n");
            }
            Map<String, List<String[]>> enums = new LinkedHashMap<>();
            for (String filename : Play.getFile("app/enums").list()) {
                Class<?> clazz = Class.forName("enums." + filename.replace(".java", ""));
                EnumClass ec = clazz.getAnnotation(EnumClass.class);
                if (ec == null || !ec.visible()) {
                    continue;
                }
                Method method = clazz.getMethod("values");
                BaseEnum[] values = (BaseEnum[]) method.invoke(null, null);
                List<String[]> list = new ArrayList<>();
                for (BaseEnum value : values) {
                    list.add(new String[]{value.code() + "", value.intro()});
                }
                enums.put(ec.name(), list);
            }
            
            String url = request.url;
            Method method = request.invokedMethod;
            ActionMethod am = method.getAnnotation(ActionMethod.class);
            String api = am.name();
            List<Object[]> param = new ArrayList<>();
            Class one = null;
            if (request.invokedMethod.getParameterCount() > 0) {
                Type type = request.invokedMethod.getParameterTypes()[0];
                if (OneData.class.isAssignableFrom((Class<?>) type)) {
                    one = (Class<OneData>) type;
                }
            }
            if (one != null) {
                if (StringUtils.isNotBlank(am.param())) {
                    for (String p : StringUtils.split(am.param(), ",")) {
                        String _p = p.replace("-", "").replace("+", "");
                        Object[] o = new Object[6];
                        Field f = one.getField(_p);
                        DataField df = f.getAnnotation(DataField.class);
                        o[0] = f.getName();
                        o[1] = df.name();
                        o[2] = f.getType().getSimpleName();
                        o[3] = p.startsWith("-") ? "否" : "是";
                        o[4] = df.demo();
                        o[5] = df.comment();
                        param.add(o);
                    }
                } else {
                    for (Field f : one.getDeclaredFields()) {
                        if (am.except().contains(f.getName())) {
                            continue;
                        }
                        DataField df = f.getAnnotation(DataField.class);
                        if (df == null) {
                            continue;
                        }
                        if (!f.getName().endsWith(ID) && !df.enable()) {
                            continue;
                        }
                        Object[] o = new Object[6];
                        o[0] = f.getName();
                        o[1] = df.name();
                        o[2] = f.getType().getSimpleName();
                        o[3] = "是";
                        o[4] = df.demo();
                        o[5] = df.comment();
                        param.add(o);
                    }
                }
            } else {
                Parameter[] parameters = method.getParameters();
                if (parameters.length > 0) {
                    Class controller = request.controllerClass;
                    String methodName = method.getName();
                    ClassPool pool = ClassPool.getDefault();
                    ClassClassPath ccPath = new ClassClassPath(controller);
                    pool.insertClassPath(ccPath);
                    String localPath = Play.getFile("tmp/classes").getAbsolutePath();
                    pool.insertClassPath(localPath);
                    CtClass cc = pool.get(controller.getName());
                    CtMethod cm = cc.getDeclaredMethod(methodName);
                    MethodInfo methodInfo = cm.getMethodInfo();
                    CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
                    LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute
                            .getAttribute(LocalVariableAttribute.tag);
                    String[] paramNames = new String[cm.getParameterTypes().length];
                    int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;
                    for (int i = 0; i < paramNames.length; i++) {
                        paramNames[i] = attr.variableName(i + pos);
                    }
                    for (Parameter p : parameters) {
                        ParamField pf = p.getAnnotation(ParamField.class);
                        if (pf != null) {
                            Object[] o = new Object[6];
                            o[0] = paramNames[Integer.parseInt(p.getName().replace("arg", ""))];
                            o[1] = pf.name();
                            o[2] = p.getType().getSimpleName();
                            o[3] = pf.required() ? "是" : "否";
                            o[4] = pf.demo();
                            o[5] = pf.comment();
                            param.add(o);
                        }
                    }
                }
            }
            Class<? extends Data>[] clazz = am.clazz();
            String result = "";
            if (clazz.length == 1 && OneData.class.isAssignableFrom(clazz[0])) {
                result = JSONUtils.format(new Gson().toJson(((OneData) clazz[0].newInstance()).doc()));
            } else if (clazz.length == 2 && PageData.class.isAssignableFrom(clazz[0])
                    && OneData.class.isAssignableFrom(clazz[1])) {
                result = JSONUtils.format(new Gson()
                        .toJson(((PageData) clazz[0].newInstance()).doc((Class<? extends OneData>) clazz[1])));
            }
            renderTemplate("doc.html", url, api, param, result, codes, enums);
        } catch (Exception e) {
            e.printStackTrace();
            renderHtml("文档错误:" + e.getMessage());
        } finally {
        }
    }
    
}