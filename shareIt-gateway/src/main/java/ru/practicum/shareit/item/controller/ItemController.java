package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.client.CommentClient;
import ru.practicum.shareit.item.client.ItemClient;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemClientDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.validation.CommentCreate;
import ru.practicum.shareit.item.validation.ItemCreate;
import ru.practicum.shareit.item.validation.ItemUpdate;


@RestController
@Validated
@RequiredArgsConstructor
public class ItemController {

    private final ItemClient itemClient;
    private final CommentClient commentClient;

    @GetMapping("/items/{itemId}")
    public ResponseEntity<Object> getItemById(@PathVariable Long itemId,
                                              @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.getItemById(itemId, userId);
    }

    @PostMapping("/items")
    public ResponseEntity<Object> addItem(@ItemCreate @RequestBody ItemInputDto itemInputDto,
                                          @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemClient.addItem(itemInputDto, userId);
    }

    @PatchMapping("/items/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @ItemUpdate @RequestBody ItemInputDto itemInputDto,
                                             @RequestHeader("X-Sharer-User-Id") Long userId) {
        itemInputDto.setId(itemId);
        ItemClientDto dto = ItemClientDto.builder()
                .itemId(itemId)
                .userId(userId)
                .itemInputDto(itemInputDto)
                .build();
        return itemClient.updateItem(dto);
    }

    @GetMapping("/items/search")
    public ResponseEntity<Object> searchItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestParam String text,
                                             @RequestParam(required = false, defaultValue = "0") Integer from,
                                             @RequestParam(required = false, defaultValue = "10") Integer size) {
        ItemClientDto dto = ItemClientDto.builder()
                .userId(userId)
                .searchText(text)
                .from(from)
                .size(size)
                .build();
        return itemClient.searchItem(dto);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable Long itemId) {
        return itemClient.deleteItemById(itemId);
    }

    @GetMapping("/items")
    public ResponseEntity<Object> getAllItemsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        ItemClientDto dto = ItemClientDto.builder()
                .userId(userId)
                .from(from)
                .size(size)
                .build();
        return itemClient.getAllItemsOfUser(dto);
    }

    @PostMapping("/items/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                             @RequestHeader("X-Sharer-User-Id") Long authorId,
                                             @CommentCreate @RequestBody CommentInputDto commentInputDto) {
        ItemClientDto dto = ItemClientDto.builder()
                .commentInputDto(commentInputDto)
                .itemId(itemId)
                .userId(authorId)
                .build();
        return commentClient.addComment(dto);
    }

}
