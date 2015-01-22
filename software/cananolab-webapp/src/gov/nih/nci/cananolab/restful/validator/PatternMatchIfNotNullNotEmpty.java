package gov.nih.nci.cananolab.restful.validator;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;
import javax.validation.constraints.Pattern;

@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CustomPatternValidator.class)
@Documented
public @interface PatternMatchIfNotNullNotEmpty {

    String message() default "{javax.validation.constraints.Pattern.message}";
    
    Pattern.Flag[] flags() default {};
    		
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
    String regexp() default "";
    
    String regexpName();
    String messageSource() default "";
    String messageKey() default "";
}
