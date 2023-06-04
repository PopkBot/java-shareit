package ru.practicum.gateway.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.user.client.UserClient;
import ru.practicum.gateway.user.dto.UserInputDto;
import ru.practicum.gateway.user.validation.UserCreate;
import ru.practicum.gateway.user.validation.UserUpdate;


@RestController
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserClient userClient;


    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@UserCreate @RequestBody UserInputDto user) {
        return userClient.createUser(user);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Object> updateUser(@UserUpdate @RequestBody UserInputDto user, @PathVariable Long userId) {
        return userClient.updateUser(user, userId);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Object> deleteUserByID(@PathVariable Long userId) {
        return userClient.deleteUserByID(userId);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers() {
        return userClient.getAllUsers();
    }
}
