package annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.*;

@Retention(RUNTIME)
@Target(ElementType.METHOD)
public @interface GetRequest {
    public String value() default "";
}
