package ru.practicum.shareit.server.request.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.request.dto.ItemRequestDto;
import ru.practicum.shareit.server.request.model.ItemRequest;

@Component
public class ItemRequestMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ItemRequestMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public ItemRequestDto convertToItemRequestDto(ItemRequest itemRequest) {
        ItemRequestDto itemRequestDto = modelMapper.map(itemRequest, ItemRequestDto.class);
        itemRequestDto.setCreated(itemRequest.getCreated().toLocalDateTime());
        return itemRequestDto;
    }

}
