package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemMapper itemMapper;


    @Override
    public List<ItemDto> getAllItemsOfUser(Long userId) {
        if (!userRepository.isContainUser(userId)) {
            throw new ObjectNotFoundException("user not found");
        }
        List<ItemDto> itemDtos = itemRepository.getAllItemsOfUser(userId).stream()
                .map(itemMapper::convertToUserDto).collect(Collectors.toList());
        log.info("list of user`s {} items is returned", userId);
        return itemDtos;
    }

    @Override
    public ItemDto getItemById(Long itemId) {

        if (!itemRepository.isContainItem(itemId)) {
            throw new ObjectNotFoundException("item not found");
        }
        ItemDto itemDto = itemMapper.convertToUserDto(itemRepository.getItemById(itemId));
        log.info("item {} is returned", itemDto);
        return itemDto;
    }

    @Override
    public ItemDto addItem(Item item, Long userId) {
        if (itemRepository.isContainItem(item.getId())) {
            throw new ObjectAlreadyExists("item already exists");
        }
        if (!userRepository.isContainUser(userId)) {
            throw new ObjectNotFoundException("user not found");
        }
        Item addedItem = itemRepository.addItem(item, userId);
        log.info("item {} is added", addedItem);
        return itemMapper.convertToUserDto(addedItem);

    }

    @Override
    public ItemDto updateItem(Item item, Long userId) {
        if (!itemRepository.isContainItem(item.getId())) {
            throw new ObjectNotFoundException("item not exists");
        }
        if (!itemRepository.isContainUser(userId)) {
            throw new ObjectNotFoundException("user didn`t published any item");
        }
        if (!itemRepository.isPertainToUser(item.getId(), userId)) {
            throw new ObjectNotFoundException("user doesn`t pertain this item");
        }
        Item updatedItem = itemRepository.updateItem(item, userId);
        log.info("item {} is updated", updatedItem);
        return itemMapper.convertToUserDto(updatedItem);
    }

    @Override
    public List<ItemDto> searchItem(String text, Long userId) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text).stream()
                .map(itemMapper::convertToUserDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto deleteItem(Long itemId) {
        if (!itemRepository.isContainItem(itemId)) {
            throw new ObjectNotFoundException("item not exists");
        }
        Item removedItem = itemRepository.deleteItem(itemId);
        log.info("item {} is deleted", removedItem);
        return itemMapper.convertToUserDto(removedItem);
    }

}
