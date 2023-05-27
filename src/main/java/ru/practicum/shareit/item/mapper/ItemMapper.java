package ru.practicum.shareit.item.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemMapper {

    private final ModelMapper modelMapper;
    private final CommentMapper commentMapper;

    @Autowired
    public ItemMapper(CommentMapper commentMapper) {
        this.commentMapper = commentMapper;
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    }


    public ItemDto convertToItemDto(Item item) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        if (item.getComments() != null) {
            List<CommentDto> commentDtos = item.getComments()
                    .stream()
                    .map(commentMapper::convertToCommentDto)
                    .collect(Collectors.toList());

            itemDto.setComments(commentDtos);
        }
        return itemDto;
    }

    public ItemDto convertToItemDtoForOwner(
            Item item, BookerDtoInItem nextBooking, BookerDtoInItem lastBooking) {
        ItemDto itemDto = modelMapper.map(item, ItemDto.class);
        itemDto.setNextBooking(nextBooking);
        itemDto.setLastBooking(lastBooking);
        if (item.getComments() != null) {
            List<CommentDto> commentDtos = item.getComments()
                    .stream()
                    .map(commentMapper::convertToCommentDto)
                    .collect(Collectors.toList());
            itemDto.setComments(commentDtos);
        }
        return itemDto;
    }

    public Item convertToItem(ItemInputDto inputDto){
        return modelMapper.map(inputDto, Item.class);
    }

    public Item convertToItem(ItemDto itemDto) {
        return modelMapper.map(itemDto, Item.class);
    }

}
