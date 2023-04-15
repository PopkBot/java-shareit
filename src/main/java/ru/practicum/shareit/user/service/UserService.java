package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public interface UserService {

    UserDto createUser(User user);

    UserDto getUserById(Long userId);

    UserDto updateUser(User user, Long userId);

    UserDto deleteUser(Long userId);


}
