package ru.practicum.gateway.user.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserCreateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserCreate {
    String message() default "{Request is invalid for adding}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
