package ru.practicum.shareit.server.item.service;

import ru.practicum.shareit.server.item.dto.CommentDto;
import ru.practicum.shareit.server.item.dto.CommentInputDto;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.dto.ItemInputDto;

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
