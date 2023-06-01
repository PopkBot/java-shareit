package ru.practicum.shareit.user.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = UserUpdateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUpdate {
    String message() default "{Item is invalid for update}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
