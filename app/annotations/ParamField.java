package annotations;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(PARAMETER)
public @interface ParamField {
    
    String name() default "";
    
    String demo() default "";
    
    String comment() default "";
    
    boolean required() default true;
    
}
