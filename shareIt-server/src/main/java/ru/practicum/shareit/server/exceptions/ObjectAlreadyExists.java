package ru.practicum.shareit.server.exceptions;

public class ObjectAlreadyExists extends RuntimeException {

    public ObjectAlreadyExists(String message) {
        super(message);
    }

}
