package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserInputDto;

import java.util.List;

public interface UserService {

    UserDto createUser(UserInputDto userInputDto);

    UserDto getUserById(Long userId);

    UserDto updateUser(UserInputDto userInputDto, Long userId);

    UserDto deleteUser(Long userId);

    List<UserDto> getAllUsers();

}
