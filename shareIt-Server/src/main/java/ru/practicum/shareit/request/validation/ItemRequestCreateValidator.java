package ru.practicum.shareit.request.validation;

import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ItemRequestCreateValidator implements ConstraintValidator<ItemRequestCreate, ItemRequestInputDto> {

    @Override
    public boolean isValid(ItemRequestInputDto itemRequestInputDto,
                           ConstraintValidatorContext constraintValidatorContext) {
        if (itemRequestInputDto == null) {
            throw new ValidationException("Item request cannot be null");
        }
        if (itemRequestInputDto.getDescription() == null || itemRequestInputDto.getDescription().isBlank()) {
            throw new ValidationException("Description cannot be blank");
        }
        return true;
    }
}