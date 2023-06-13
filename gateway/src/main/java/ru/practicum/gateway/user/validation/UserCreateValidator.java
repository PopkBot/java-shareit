package ru.practicum.gateway.user.validation;

import org.apache.commons.validator.routines.EmailValidator;
import ru.practicum.gateway.exceptions.ValidationException;
import ru.practicum.gateway.user.dto.UserInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserCreateValidator implements ConstraintValidator<UserCreate, UserInputDto> {

    @Override
    public boolean isValid(UserInputDto user,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (user.getName() == null || user.getName().isBlank()) {
            throw new ValidationException("name cannot be blank");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("email cannot be blank");
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("wrong email format");
        }
        return true;
    }
}