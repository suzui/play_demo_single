package annotations;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(FIELD)
public @interface DataField {
    
    String name() default "";
    
    String demo() default "";
    
    String comment() default "";
    
    boolean enable() default true;
}
