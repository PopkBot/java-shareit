package ru.practicum.shareit.booking.validation;



import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = BookingCreateValidator.class)
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface BookingCreate {

    String message() default "{Booking is invalid to adding}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
