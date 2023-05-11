package ru.practicum.shareit.item.validation;

import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ItemCreateValidator implements ConstraintValidator<ItemCreate, Item> {

    @Override
    public boolean isValid(Item item, ConstraintValidatorContext constraintValidatorContext) {
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


