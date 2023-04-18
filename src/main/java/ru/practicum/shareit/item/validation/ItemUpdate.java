package ru.practicum.shareit.item.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ItemUpdateValidator.class)
@Target( { ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ItemUpdate {
    String message() default "{Item is invalid for update}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
