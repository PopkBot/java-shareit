package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.validation.CommentCreate;
import ru.practicum.shareit.item.validation.ItemCreate;
import ru.practicum.shareit.item.validation.ItemUpdate;

import java.util.List;

@Slf4j
@RestController
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/items/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId,
                               @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("item {} requested", itemId);
        return itemService.getItemById(itemId, userId);
    }

    @PostMapping("/items")
    public ItemDto addItem(@ItemCreate @RequestBody Item item,
                           @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("adding new item {} requested", item);
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/items/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @ItemUpdate @RequestBody Item item,
                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("updating item requested");
        item.setId(itemId);
        return itemService.updateItem(item, userId);
    }

    @GetMapping("/items/search")
    public List<ItemDto> searchItem(@RequestParam String text,
                                    @RequestParam(required = false,defaultValue = "0")Integer from,
                                    @RequestParam(required = false,defaultValue = "10")Integer size) {
        log.info("search for {}", text);
        return itemService.searchItem(text,from,size);
    }

    @DeleteMapping("/items/{itemId}")
    public ItemDto deleteItemById(@PathVariable Long itemId) {
        log.info("deleting item {} requested", itemId);
        return itemService.deleteItem(itemId);
    }

    @GetMapping("/items")
    public List<ItemDto> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                           @RequestParam(required = false,defaultValue = "0")Integer from,
                                           @RequestParam(required = false,defaultValue = "10")Integer size) {
        log.info("all items of user {} are requested", userId);
        return itemService.getAllItemsOfUser(userId,from,size);
    }

    @PostMapping("/items/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestHeader("X-Sharer-User-Id") Long authorId,
                                 @CommentCreate @RequestBody CommentInputDto commentInputDto) {
        commentInputDto.setAuthorId(authorId);
        commentInputDto.setItemId(itemId);
        log.info("Comment {} adding is requested", commentInputDto);
        return itemService.addComment(commentInputDto);
    }

}
