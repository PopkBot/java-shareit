package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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
    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;


    @Override
    public List<ItemDto> getAllItemsOfUser(Long userId) {
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user not found"));
        List<ItemDto> itemDtos = itemRepository.findAllByUserId(userId).stream()
                .map(item -> {
                            BookerDtoInItem bookingDtoNext = bookingMapper.convertToBookingDtoInItem(
                                    bookingRepository.getNextBooking(nowStr, item.getId()));
                            BookerDtoInItem bookingDtoLast = bookingMapper.convertToBookingDtoInItem(
                                    bookingRepository.getLastBooking(nowStr, item.getId()));
                            return itemMapper.convertToItemDtoForOwner(
                                    item, bookingDtoNext, bookingDtoLast);
                        }
                ).collect(Collectors.toList());
        log.info("list of user`s {} items is returned", userId);
        return itemDtos;
    }

    @Override
    public ItemDto getItemById(Long itemId, Long userId) {

        Item item = itemRepository.findById(itemId).orElseThrow(
                () -> new ObjectNotFoundException("item not found")
        );
        ItemDto itemDto;
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        if (item.getUser().getId().equals(userId)) {
            BookerDtoInItem bookingDtoNext = bookingMapper.convertToBookingDtoInItem(
                    bookingRepository.getNextBooking(nowStr, item.getId()));
            BookerDtoInItem bookingDtoLast = bookingMapper.convertToBookingDtoInItem(
                    bookingRepository.getLastBooking(nowStr, item.getId()));
            itemDto = itemMapper.convertToItemDtoForOwner(item, bookingDtoNext, bookingDtoLast);
        } else {
            itemDto = itemMapper.convertToItemDto(item);
        }
        log.info("item {} is returned", itemDto);
        return itemDto;
    }

    @Override
    @Transactional
    public ItemDto addItem(Item item, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user not found"));
        item.setUser(user);
        Item addedItem = itemRepository.save(item);
        log.info("item {} is added", addedItem);
        return itemMapper.convertToItemDto(addedItem);

    }

    @Override
    @Transactional
    public ItemDto updateItem(Item item, Long userId) {
        Item itemToUpdate = itemRepository.findById(item.getId()).orElseThrow(
                () -> new ObjectNotFoundException("item not exists")
        );
        itemRepository.findByIdAndUserId(item.getId(), userId).orElseThrow(
                () -> new ObjectNotFoundException("user doesn`t pertain this item")
        );
        updateItemParams(itemToUpdate, item);
        itemToUpdate = itemRepository.save(itemToUpdate);
        log.info("item {} is updated", itemToUpdate);
        return itemMapper.convertToItemDto(itemToUpdate);
    }

    private void updateItemParams(Item itemTo, Item itemFrom) {
        if (itemFrom.getName() != null) {
            itemTo.setName(itemFrom.getName());
        }
        if (itemFrom.getDescription() != null) {
            itemTo.setDescription(itemFrom.getDescription());
        }
        if (itemFrom.getAvailable() != null) {
            itemTo.setAvailable(itemFrom.getAvailable());
        }
    }

    @Override
    public List<ItemDto> searchItem(String text, Long userId) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        text = "%" + text + "%";
        text = text.toUpperCase();
        return itemRepository.searchByQueryText(text).stream()
                .map(itemMapper::convertToItemDto).collect(Collectors.toList());
    }

    @Override
    public ItemDto deleteItem(Long itemId) {
        Item itemToRemove = itemRepository.findById(itemId).orElseThrow(
                () -> new ObjectNotFoundException("item not exists")
        );
        itemRepository.delete(itemToRemove);
        log.info("item {} is deleted", itemToRemove);
        return itemMapper.convertToItemDto(itemToRemove);
    }

    @Override
    @Transactional
    public CommentDto addComment(CommentInputDto commentInputDto) {

        User author = userRepository.findById(commentInputDto.getAuthorId()).orElseThrow(
                () -> new ObjectNotFoundException("User not found")
        );
        Item item = itemRepository.findById(commentInputDto.getItemId()).orElseThrow(
                () -> new ObjectNotFoundException("Item not found")
        );
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        Long count = bookingRepository.countByBookerIdAndItemIdAndStatus(author.getId(),
                commentInputDto.getItemId(), Status.APPROVED.toString(), nowStr);
        if (count == 0) {
            throw new ValidationException("User hasn`t booked this item");
        }
        if (item.getUser().getId().equals(author.getId())) {
            throw new ValidationException("Owner cannot leave comments");
        }
        Comment comment = commentRepository.save(makeComment(commentInputDto, author));
        item.getComments().add(comment);
        log.info("Comment {} is added", comment);
        return commentMapper.convertToCommentDto(comment);

    }

    private Comment makeComment(CommentInputDto commentInputDto, User author) {
        Comment comment = commentMapper.convertToComment(commentInputDto);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().getEpochSecond());
        return comment;
    }


}
