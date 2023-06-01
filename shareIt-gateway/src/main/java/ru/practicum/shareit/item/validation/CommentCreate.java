package ru.practicum.shareit.item.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CommentCreateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentCreate {
    String message() default "{Comment is invalid for adding}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
