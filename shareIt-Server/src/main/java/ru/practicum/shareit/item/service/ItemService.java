package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInputDto;

import java.util.List;

public interface ItemService {

    List<ItemDto> getAllItemsOfUser(Long userId, Integer from, Integer size);

    ItemDto getItemById(Long itemId, Long userId);

    ItemDto addItem(ItemInputDto itemInputDto, Long userId);

    ItemDto updateItem(ItemInputDto itemInputDto, Long userId);

    List<ItemDto> searchItem(String text, Integer from, Integer size);

    ItemDto deleteItem(Long itemId);

    CommentDto addComment(CommentInputDto commentInputDto);


}
