package ru.practicum.shareit.booking.validation;


import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.exceptions.ValidationException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;


public class BookingCreateValidator implements ConstraintValidator<BookingCreate, BookingInputDto> {

    @Override
    public boolean isValid(BookingInputDto booking, ConstraintValidatorContext constraintValidatorContext) {

        LocalDateTime start = booking.getStart();
        LocalDateTime end = booking.getEnd();
        LocalDateTime now = LocalDateTime.now();

        if (start == null) {
            throw new ValidationException("Booking start date cannot be null");
        }
        if (end == null) {
            throw new ValidationException("Booking end date cannot be null");
        }
        if (start.equals(end)) {
            throw new ValidationException("Booking start date cannot be equal to end date");
        }
        if (start.isAfter(end)) {
            throw new ValidationException("Booking start date cannot be later end date");
        }
        if (end.isBefore(now)) {
            throw new ValidationException("Booking end date cannot be in the past");
        }
        if (start.isBefore(now)) {
            throw new ValidationException("Booking start date cannot be in the past");
        }
        return true;
    }
}
