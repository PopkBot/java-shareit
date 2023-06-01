package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserDto createUser(UserInputDto userInputDto) {
        User createdUser = userMapper.convertToUser(userInputDto);
        try {
            createdUser = userRepository.save(createdUser);
        } catch (RuntimeException e) {
            throw new ObjectAlreadyExists("unable to create user: user already exists");
        }
        log.info("user {} is added", createdUser);
        return userMapper.convertToUserDto(createdUser);
    }

    @Override
    public UserDto getUserById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("unable to get user: user not exists")
        );
        UserDto userDto = userMapper.convertToUserDto(user);
        log.info("userDto {} is returned", userDto);
        return userDto;
    }

    @Override
    @Transactional
    public UserDto updateUser(UserInputDto user, Long userId) {
        User userToUpdate = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("unable to update user: user not found")
        );
        if (userRepository.findByEmailIgnoreCaseAndIdNot(user.getEmail(), userId) != null) {
            throw new ObjectAlreadyExists("unable to update user: same user already exists");
        }
        updateUserParams(userToUpdate, user);
        userToUpdate = userRepository.save(userToUpdate);
        log.info("user {} is updated", userToUpdate);
        return userMapper.convertToUserDto(userToUpdate);

    }

    private void updateUserParams(User userTo, UserInputDto userFrom) {
        if (userFrom.getEmail() != null) {
            userTo.setEmail(userFrom.getEmail());
        }
        if (userFrom.getName() != null) {
            userTo.setName(userFrom.getName());
        }
    }

    @Override
    public UserDto deleteUser(Long userId) {
        User userToDelete = userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("unable to delete user: user not exists")
        );
        userRepository.deleteById(userId);
        UserDto userDto = userMapper.convertToUserDto(userToDelete);
        log.info("user {} is deleted", userDto);
        return userDto;
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> userDtos = userRepository.findAll().stream()
                .map(userMapper::convertToUserDto).collect(Collectors.toList());
        log.info("all users are returned");
        return userDtos;

    }
}
