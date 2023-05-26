package ru.practicum.shareit.request.sevice;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto addRequest(ItemRequestInputDto inputDto, Long userId);

    List<ItemRequestDto> getItemRequestsOfUser(Long userId);

    List<ItemRequestDto> getAllRequestsPaged(Long userId,Integer from,Integer size);

    ItemRequestDto getItemRequestById(Long userId, Long requestId);

}
