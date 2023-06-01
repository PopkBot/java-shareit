package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.client.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.validation.ItemRequestCreate;


@RestController
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping("/requests")
    public ResponseEntity<Object> addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @ItemRequestCreate @RequestBody ItemRequestInputDto inputDto) {
        return itemRequestClient.addRequest(userId, inputDto);
    }

    @GetMapping("/requests")
    public ResponseEntity<Object> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemRequestClient.getRequestsOfUser(userId);
    }

    @GetMapping("/requests/all")
    public ResponseEntity<Object> getAllRequestsPaged(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                      @RequestParam(required = false, defaultValue = "0") Integer from,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size) {
        return itemRequestClient.getAllRequestsPaged(userId, from, size);
    }

    @GetMapping("/requests/{requestId}")
    public ResponseEntity<Object> getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long requestId) {
        return itemRequestClient.getRequestById(userId, requestId);
    }

}
