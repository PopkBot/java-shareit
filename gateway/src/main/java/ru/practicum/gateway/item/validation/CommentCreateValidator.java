package ru.practicum.gateway.item.validation;

import ru.practicum.gateway.exceptions.ValidationException;
import ru.practicum.gateway.item.dto.CommentInputDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CommentCreateValidator implements ConstraintValidator<CommentCreate, CommentInputDto> {

    @Override
    public boolean isValid(CommentInputDto comment, ConstraintValidatorContext constraintValidatorContext) {

        if (comment.getText() == null || comment.getText().isBlank()) {
            throw new ValidationException("Comment text cannot be empty");
        }
        return true;
    }
}