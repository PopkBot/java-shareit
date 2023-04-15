package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("item {} requested", itemId);
        return itemService.getItemById(itemId);
    }

    @PostMapping("/items")
    public ItemDto addItem(@RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("adding new item {} requested", item);
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/items/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("updating item requested");
        item.setId(itemId);
        return itemService.updateItem(item, userId);
    }

    @GetMapping("/items/search")
    public List<ItemDto> searchItem(@RequestParam String text, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("search for {}", text);
        return itemService.searchItem(text, userId);
    }

    @DeleteMapping("/items/{itemId}")
    public ItemDto deleteItemById(@PathVariable Long itemId) {
        log.info("deleting item {} requested", itemId);
        return itemService.deleteItem(itemId);
    }

    @GetMapping("/items")
    public List<ItemDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("all items of user {} are requested", userId);
        return itemService.getAllItemsOfUser(userId);
    }

}
