package ru.practicum.shareit.user.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.model.User;

@Component
public class UserMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public UserMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public UserDto convertToUserDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    public User convertToUser(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }

    public User convertToUser(UserInputDto userInputDto) {
        return modelMapper.map(userInputDto, User.class);
    }

}
