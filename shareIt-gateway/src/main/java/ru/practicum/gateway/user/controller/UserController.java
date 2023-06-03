package ru.practicum.gateway.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.gateway.user.client.UserClient;
import ru.practicum.gateway.user.dto.UserInputDto;

import javax.validation.Valid;


@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping("/users/{userId}")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        return userClient.getUserById(userId);
    }

    @PostMapping("/users")
    public ResponseEntity<Object> createUser(@Valid @RequestBody UserInputDto user) {
        return userClient.createUser(user);
    }

    @PatchMapping("/users/{userId}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody UserInputDto user, @PathVariable Long userId) {
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
