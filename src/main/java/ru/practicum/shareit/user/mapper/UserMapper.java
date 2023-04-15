package ru.practicum.shareit.user.mapper;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final ModelMapper modelMapper;

    public UserDto convertToUserDto(User user){
        return modelMapper.map(user, UserDto.class);
    }

    public User convertToUser(UserDto userDto){
        return modelMapper.map(userDto, User.class);
    }

}
