package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    List<ItemDto> getAllItemsOfUser(Long userId);

    ItemDto getItemById(Long itemId, Long userId);

    ItemDto addItem(Item item, Long userId);

    ItemDto updateItem(Item item, Long userId);

    List<ItemDto> searchItem(String text, Long userId);

    ItemDto deleteItem(Long itemId);

    CommentDto addComment(CommentInputDto commentInputDto);


}
