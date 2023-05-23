package ru.practicum.shareit.request.sevice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemRequestServiceImp implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper itemRequestMapper;

    @Transactional
    @Override
    public ItemRequestDto addRequest(ItemRequestInputDto inputDto, Long userId) {

        User requestingUser = userRepository.findById(userId).orElseThrow(
                ()-> new ObjectNotFoundException("User not found")
        );
        ItemRequest itemRequest = itemRequestRepository.save(makeItemRequest(inputDto,requestingUser));
        log.info("added item request {}",itemRequest);
        return itemRequestMapper.convertToItemRequestDto(itemRequest);
    }

    ItemRequest makeItemRequest(ItemRequestInputDto inputDto, User requestingUser){
        return ItemRequest.builder()
                .description(inputDto.getDescription())
                .created(ZonedDateTime.now(ZoneId.systemDefault()))
                .items(new ArrayList<>())
                .requestingUser(requestingUser)
                .build();
    }

    @Override
    public List<ItemRequestDto> getItemRequestsOfUser(Long userId) {

         userRepository.findById(userId).orElseThrow(
                 ()-> new ObjectNotFoundException("user not found")
         );
         List<ItemRequestDto> itemRequestDtos = itemRequestRepository.getRequestsOfUser(userId).stream()
                 .map(itemRequestMapper::convertToItemRequestDto).collect(Collectors.toList());
         log.info("list of requests is returned {}",itemRequestDtos);
         return itemRequestDtos;
    }

    @Override
    public List<ItemRequestDto> getAllRequestsPaged(Long userId,Integer from, Integer size) {

        if(size<=0 || from<0){
            throw new ValidationException("invalid page parameters");
        }
        if(userRepository.findById(userId).isEmpty()){
            userId=-1L;
        }
        Sort sortByDate = Sort.by(Sort.Direction.ASC,"created");
        Pageable page = PageRequest.of(from,size,sortByDate);
        Page<ItemRequest> itemRequestPage = itemRequestRepository.findAllWithOutRequestingUser(userId,page);
        List<ItemRequestDto> itemRequestDtos =  itemRequestPage.getContent().stream()
                .map(itemRequestMapper::convertToItemRequestDto)
                .collect(Collectors.toList());
        log.info("Page of all item requests is returned {}",itemRequestDtos);
        return itemRequestDtos;
    }

    @Override
    public ItemRequestDto getItemRequestById(Long userId,Long requestId) {

       userRepository.findById(userId).orElseThrow(
                ()-> new ObjectNotFoundException("user not found")
        );
        ItemRequest itemRequest = itemRequestRepository.findById(requestId).orElseThrow(
                ()-> new ObjectNotFoundException("item request not found")
        );
        log.info("item request is returned {}",itemRequest);
        return itemRequestMapper.convertToItemRequestDto(itemRequest);
    }
}
