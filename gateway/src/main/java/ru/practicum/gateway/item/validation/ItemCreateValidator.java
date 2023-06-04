package ru.practicum.gateway.item.validation;

import ru.practicum.gateway.exceptions.ValidationException;
import ru.practicum.gateway.item.dto.ItemInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ItemCreateValidator implements ConstraintValidator<ItemCreate, ItemInputDto> {

    @Override
    public boolean isValid(ItemInputDto item, ConstraintValidatorContext constraintValidatorContext) {
        if (item.getName() == null || item.getName().isBlank()) {
            throw new ValidationException("name cannot be blank");
        }
        if (item.getDescription() == null || item.getDescription().isBlank()) {
            throw new ValidationException("description cannot be blank");
        }
        if (item.getAvailable() == null) {
            throw new ValidationException("available cannot be null");
        }
        return true;
    }
}


