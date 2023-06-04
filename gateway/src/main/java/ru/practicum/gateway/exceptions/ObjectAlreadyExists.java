package ru.practicum.gateway.exceptions;

public class ObjectAlreadyExists extends RuntimeException {

    public ObjectAlreadyExists(String message) {
        super(message);
    }

}
