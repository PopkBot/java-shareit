package ru.practicum.gateway.validators;

import org.springframework.stereotype.Service;
import ru.practicum.gateway.exceptions.ValidationException;

@Service
public class PageValidatorImp implements PageValidator {
    @Override
    public void validatePagination(Integer from, Integer size) {
        if (size <= 0 || from < 0) {
            throw new ValidationException("invalid page parameters");
        }
    }
}
