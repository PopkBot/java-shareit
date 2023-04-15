package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;



@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDto createUser(User user) {
        if(userRepository.isContainUser(user)){
            throw new ObjectAlreadyExists("unable to create user: user already exists");
        }
        UserDto userDto = userMapper.convertToUserDto(userRepository.addUser(user));
        log.info("user {} is added",user);
        return userDto;
    }

    @Override
    public UserDto getUserById(Long userId) {
        UserDto userDto = userMapper.convertToUserDto(userRepository.getUserById(userId));
        log.info("userDto {} is returned",userDto);
        return userDto;
    }

    @Override
    public UserDto updateUser(User user, Long userId) {
        if(!userRepository.isContainUser(userId)){
            throw new ObjectNotFoundException("unable to update user: user not found");
        }
        if(userRepository.isContainUser(user)){
            throw new ObjectAlreadyExists("unable to update user: same user already exists");
        }
        UserDto userDto = userMapper.convertToUserDto(userRepository.updateUser(user,userId));
        log.info("user {} is updated",userDto);
        return userDto;
    }

    @Override
    public UserDto deleteUser(Long userId) {
        if(!userRepository.isContainUser(userId)){
            throw new ObjectNotFoundException("unable to delete user: user not exists");
        }
        UserDto userDto = userMapper.convertToUserDto(userRepository.deleteUser(userId));
        log.info("user {} is deleted",userDto);
        return userDto;
    }
}
