package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.CustomPageRequest;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
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
    private final ItemRequestRepository itemRequestRepository;

    /*
    Реализовал пагинацию через интерфейс Pageable только здесь, на случай если этот вариант не оптимальный.
    Если я правильно понимаю, AbstractPageRequest имплементирующий Pageable автоматически добавляет в
    SQL запрос OFFSET и LIMIT, причем AbstractPageRequest получает значение OFFSET простым перемножением page№ * size.
    Задание предлагает использовать PageRequest и переработать входные from и size, чтобы произведение
    page№ * size равнялось from. Есть ли преимущества Pageable в данном случае, которые оправдывают
    такие лишние преобразования?
     */

    @Override
    public List<ItemDto> getAllItemsOfUser(Long userId, Integer from, Integer size) {
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user not found"));
        if (size <= 0 || from < 0) {
            throw new ValidationException("invalid page parameters");
        }
        Pageable page = new CustomPageRequest(from, size);
        Page<Item> itemPage = itemRepository.findAllByUserId(userId, page);
        List<ItemDto> itemDtos = itemPage.getContent().stream()
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
    public ItemDto addItem(ItemInputDto itemInputDto, Long userId) {

        Item item = itemMapper.convertToItem(itemInputDto);
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user not found"));
        item.setUser(user);
        if (item.getRequestId() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(item.getRequestId()).orElseThrow(
                    () -> new ObjectNotFoundException("item request not found")
            );
            itemRequest.getItems().add(item);
        }
        Item addedItem = itemRepository.save(item);
        log.info("item {} is added", addedItem);
        return itemMapper.convertToItemDto(addedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItem(ItemInputDto itemInputDto, Long userId) {
        Item itemToUpdate = itemRepository.findById(itemInputDto.getId()).orElseThrow(
                () -> new ObjectNotFoundException("item not exists")
        );
        itemRepository.findByIdAndUserId(itemInputDto.getId(), userId).orElseThrow(
                () -> new ObjectNotFoundException("user doesn`t pertain this item")
        );
        updateItemParams(itemToUpdate, itemMapper.convertToItem(itemInputDto));
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
    public List<ItemDto> searchItem(String text, Integer from, Integer size) {
        if (text == null || text.isBlank()) {
            return new ArrayList<>();
        }
        if (size <= 0 || from < 0) {
            throw new ValidationException("invalid page parameters");
        }
        text = "%" + text + "%";
        text = text.toUpperCase();
        List<Item> itemPage = itemRepository.searchByQueryText(text, from, size);
        log.info("page of items is returned {}", itemPage);
        return itemPage.stream()
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
        if (item.getComments() == null) {
            item.setComments(new HashSet<>());
        }

        if (item.getUser().getId().equals(author.getId())) {
            throw new ValidationException("Owner cannot leave comments");
        }
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        Long count = bookingRepository.countByBookerIdAndItemIdAndStatus(author.getId(),
                commentInputDto.getItemId(), Status.APPROVED.toString(), nowStr);
        if (count == 0) {
            throw new ValidationException("User hasn`t booked this item");
        }
        boolean isContainAuthor = item.getComments().stream().anyMatch(comment -> comment.getAuthor().equals(author));
        if (isContainAuthor) {
            throw new ObjectAlreadyExists("Author is allowed to leave one comment");
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
