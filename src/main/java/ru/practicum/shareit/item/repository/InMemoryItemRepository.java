package ru.practicum.shareit.item.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryItemRepository implements ItemRepository {
    private final HashMap<Long, Item> items = new HashMap<>();
    private final HashMap<Long, Set<Long>> userToItem = new HashMap<>();
    private Long itemIdCount = 1L;


    @Override
    public Item getItemById(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsOfUser(Long userId) {
        return items.keySet().stream()
                .filter(userToItem.get(userId)::contains)
                .map(items::get)
                .collect(Collectors.toList());
    }

    @Override
    public Item addItem(Item item, Long userId) {
        item.setId(itemIdCount);
        item.setUserId(userId);
        items.put(itemIdCount, item);
        putIntoUserToItemMap(item);
        itemIdCount++;
        return item;
    }

    @Override
    public Item updateItem(Item item, Long userId) {
        Item itemToUpdate = items.get(item.getId());
        if (item.getName() != null) {
            itemToUpdate.setName(item.getName());
        }
        if (item.getDescription() != null) {
            itemToUpdate.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            itemToUpdate.setAvailable(item.getAvailable());
        }
        return getItemById(item.getId());
    }

    @Override
    public boolean isContainItem(Long itemId) {
        return items.containsKey(itemId);
    }

    @Override
    public boolean isPertainToUser(Long itemId, Long userId) {
        return userToItem.get(userId).contains(itemId);
    }

    @Override
    public boolean isContainUser(Long userId) {
        return userToItem.containsKey(userId);
    }

    @Override
    public Item deleteItem(Long itemId) {
        Item itemToRemove = getItemById(itemId);
        items.remove(itemId);
        userToItem.get(itemToRemove.getUserId()).remove(itemToRemove);
        return itemToRemove;
    }

    @Override
    public List<Item> searchItem(String text) {
        List<Item> itemList = new ArrayList<>();
        String queryText = text.toLowerCase();
        for (Item item : items.values()) {
            String itemName = item.getName().toLowerCase();
            String itemDescription = item.getDescription().toLowerCase();
            if (item.getAvailable() && (itemName.contains(queryText) || itemDescription.contains(queryText))) {
                itemList.add(item);
            }
        }
        return itemList;
    }

    private void putIntoUserToItemMap(Item item) {
        Long userId = item.getUserId();
        Long itemId = item.getId();
        if (!userToItem.containsKey(userId)) {
            userToItem.put(userId, new HashSet<>(List.of(itemId)) {
            });
            return;
        }
        userToItem.get(userId).add(itemId);
    }

}
