package ru.practicum.shareit.user.validation;

import org.apache.commons.validator.routines.EmailValidator;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserUpdateValidator implements ConstraintValidator<UserUpdate, UserInputDto> {


    @Override
    public boolean isValid(UserInputDto userInputDto, ConstraintValidatorContext constraintValidatorContext) {
        if (!EmailValidator.getInstance().isValid(userInputDto.getEmail()) && userInputDto.getEmail() != null) {
            throw new ValidationException("wrong email format");
        }
        return true;
    }
}
