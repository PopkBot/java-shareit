package ru.practicum.shareit.item.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

@Component
public class ItemMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ItemMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STANDARD);
    }

    public ItemDto convertToItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    public ItemDto convertToItemDtoForOwner(Item item, BookerDtoInItem nextBooking, BookerDtoInItem lastBooking) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        itemDto.setNextBooking(nextBooking);
        itemDto.setLastBooking(lastBooking);
        return itemDto;
    }

    public Item convertToItem(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

}
