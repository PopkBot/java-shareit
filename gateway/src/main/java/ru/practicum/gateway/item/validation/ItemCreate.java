package ru.practicum.gateway.item.validation;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ItemCreateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemCreate {
    String message() default "{Item is invalid for adding}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}


