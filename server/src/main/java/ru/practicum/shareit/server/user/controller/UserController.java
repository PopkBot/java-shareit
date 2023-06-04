package ru.practicum.shareit.server.user.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.dto.UserInputDto;
import ru.practicum.shareit.server.user.service.UserService;

import java.util.List;


@RestController
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public UserDto getUserById(@PathVariable Long userId) {
        log.info("user {} is requested", userId);
        return userService.getUserById(userId);
    }

    @PostMapping("/users")
    public UserDto createUser(@RequestBody UserInputDto user) {
        log.info("adding new user {} requested", user);
        return userService.createUser(user);
    }

    @PatchMapping("/users/{userId}")
    public UserDto updateUser(@RequestBody UserInputDto user, @PathVariable Long userId) {
        log.info("updating of user {} requested", userId);
        return userService.updateUser(user, userId);
    }

    @DeleteMapping("/users/{userId}")
    public UserDto deleteUserByID(@PathVariable Long userId) {
        log.info("deleting of user {} requested", userId);
        return userService.deleteUser(userId);
    }

    @GetMapping("/users")
    public List<UserDto> getAllUsers() {
        log.info("all users are requested");
        return userService.getAllUsers();
    }
}
