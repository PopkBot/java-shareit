package ru.practicum.shareit.server.user.service;

import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.dto.UserInputDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserInputDto userInputDto);

    UserDto getUserById(Long userId);

    UserDto updateUser(UserInputDto userInputDto, Long userId);

    UserDto deleteUser(Long userId);

    List<UserDto> getAllUsers();

}
