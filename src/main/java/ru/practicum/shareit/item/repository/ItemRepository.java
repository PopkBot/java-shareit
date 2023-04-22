package ru.practicum.shareit.item.repository;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository {

    Item getItemById(Long itemId);

    List<Item> getAllItemsOfUser(Long userId);

    Item addItem(Item item, Long userId);

    Item updateItem(Item item, Long userId);

    boolean isContainItem(Long itemId);

    boolean isPertainToUser(Long itemId, Long userId);

    boolean isContainUser(Long userId);

    Item deleteItem(Long itemId);

    List<Item> searchItem(String text);


}
