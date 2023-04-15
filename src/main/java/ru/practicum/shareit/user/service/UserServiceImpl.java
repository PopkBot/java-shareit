package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(User user) {
        validateUserForCreation(user);
        if (userRepository.isContainUser(user)) {
            throw new ObjectAlreadyExists("unable to create user: user already exists");
        }
        User createdUser = userRepository.addUser(user);
        log.info("user {} is added", createdUser);
        return userMapper.convertToUserDto(createdUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserDto userDto = userMapper.convertToUserDto(userRepository.getUserById(userId));
        log.info("userDto {} is returned", userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(User user, Long userId) {
        validateUserForUpdate(user);
        if (!userRepository.isContainUser(userId)) {
            throw new ObjectNotFoundException("unable to update user: user not found");
        }
        if (userRepository.isContainUser(user, userId)) {
            throw new ObjectAlreadyExists("unable to update user: same user already exists");
        }
        User updatedUser = userRepository.updateUser(user, userId);
        log.info("user {} is updated", updatedUser);
        return userMapper.convertToUserDto(updatedUser);
    }

    @Override
    public UserDto deleteUser(Long userId) {
        if (!userRepository.isContainUser(userId)) {
            throw new ObjectNotFoundException("unable to delete user: user not exists");
        }
        UserDto userDto = userMapper.convertToUserDto(userRepository.deleteUser(userId));
        log.info("user {} is deleted", userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = userRepository.getAllUsers().stream()
                .map(userMapper::convertToUserDto).collect(Collectors.toList());
        log.info("all users are returned");
        return userDtos;
    }

    private void validateUserForCreation(User user) {
        if (user.getName().isBlank()) {
            throw new ValidationException("name cannot be blank");
        }
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ValidationException("email cannot be blank");
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("wrong email format");
        }
    }

    private void validateUserForUpdate(User user) {
        if (!EmailValidator.getInstance().isValid(user.getEmail()) && user.getEmail() != null) {
            throw new ValidationException("wrong email format");
        }
    }


}
