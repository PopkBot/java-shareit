package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.sevice.ItemRequestService;
import ru.practicum.shareit.request.validation.ItemRequestCreate;

import java.util.List;


@RestController
@Slf4j
@Validated
@RequiredArgsConstructor
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @PostMapping("/requests")
    public ItemRequestDto addRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @ItemRequestCreate @RequestBody ItemRequestInputDto inputDto) {
        log.info("adding of item request {}", inputDto);
        return itemRequestService.addRequest(inputDto, userId);
    }

    @GetMapping("/requests")
    public List<ItemRequestDto> getRequestsOfUser(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("list of item requests of user {} is requested", userId);
        return itemRequestService.getItemRequestsOfUser(userId);
    }

    @GetMapping("/requests/all")
    public List<ItemRequestDto> getAllRequestsPaged(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("list of all requests is requested from {} with page size {}", from, size);
        return itemRequestService.getAllRequestsPaged(userId, from, size);
    }

    @GetMapping("/requests/{requestId}")
    public ItemRequestDto getRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                         @PathVariable Long requestId) {
        log.info("item request {} is requested", requestId);
        return itemRequestService.getItemRequestById(userId, requestId);
    }

}
