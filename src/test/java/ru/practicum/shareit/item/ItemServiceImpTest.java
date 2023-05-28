package ru.practicum.shareit.item;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemInputDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImpTest {

    private final ItemService itemService;
    private final UserService userService;
    private final ItemMapper itemMapper;
    private final BookingService bookingService;


    @Test
    void throwExceptionOnAddingItemWhenUserNotExists() {

        ItemInputDto item = ItemInputDto.builder()
                .name("name")
                .description("description")
                .available(true)
                .build();

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> itemService.addItem(item, -1L));
        assertEquals("user not found", exception.getMessage());
    }

    @Test
    void addItemTest() {

        UserInputDto user = UserInputDto.builder()
                .name("user")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user).getId();

        ItemInputDto item = ItemInputDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        ItemDto expectedItemDto = itemMapper.convertToItemDto(itemMapper.convertToItem(item));

        ItemDto itemDto = itemService.addItem(item, userId);
        expectedItemDto.setId(itemDto.getId());
        assertEquals(expectedItemDto, itemDto);

    }

    @Test
    void throwExceptionOnItemUpdatingWhenUserIsInvalid() {

        ItemInputDto item = ItemInputDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        UserInputDto user = UserInputDto.builder()
                .name("user")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user).getId();
        item.setId(itemService.addItem(item, userId).getId());

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> itemService.updateItem(item, -1L));
        assertEquals("user doesn`t pertain this item", exception.getMessage());

        item.setId(-1L);
        exception = assertThrows(ObjectNotFoundException.class,
                () -> itemService.updateItem(item, userId));
        assertEquals("item not exists", exception.getMessage());
    }

    @Test
    void testUpdateItem() {

        ItemInputDto item = ItemInputDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        UserInputDto user = UserInputDto.builder()
                .name("user")
                .email("user@user.com")
                .build();

        Long userId = userService.createUser(user).getId();
        item.setId(itemService.addItem(item, userId).getId());

        ItemInputDto itemUpdate = ItemInputDto.builder()
                .id(item.getId())
                .name("updated")
                .description("updated")
                .available(false)
                .build();

        ItemDto expectedItemDto = itemMapper.convertToItemDto(itemMapper.convertToItem(itemUpdate));
        ItemDto itemDto = itemService.updateItem(itemUpdate, userId);
        assertEquals(expectedItemDto, itemDto);
    }

    @Test
    void testDeleteItem() {

        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> itemService.deleteItem(-1L));
        assertEquals("item not exists", exception.getMessage());

        ItemInputDto item = ItemInputDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        UserInputDto user = UserInputDto.builder()
                .name("user")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user).getId();
        item.setId(itemService.addItem(item, userId).getId());
        ItemDto expectedItemDto = itemMapper.convertToItemDto(itemMapper.convertToItem(item));
        ItemDto itemDto = itemService.deleteItem(item.getId());
        assertEquals(expectedItemDto, itemDto);
    }

    @Test
    void testExceptionsOnCommentAdding() {

        ItemInputDto item = ItemInputDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        UserInputDto owner = UserInputDto.builder()
                .name("owner")
                .email("owner@user.com")
                .build();
        Long ownerId = userService.createUser(owner).getId();
        UserInputDto booker1 = UserInputDto.builder()
                .name("booker1")
                .email("booker1@user.com")
                .build();
        Long booker1Id = userService.createUser(booker1).getId();

        item.setId(itemService.addItem(item, ownerId).getId());

        CommentInputDto commentInputDto = CommentInputDto.builder()
                .text("comment")
                .authorId(-1L)
                .itemId(-1L)
                .build();

        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> itemService.addComment(commentInputDto));
        assertEquals("User not found", oe.getMessage());

        commentInputDto.setAuthorId(booker1Id);

        oe = assertThrows(ObjectNotFoundException.class,
                () -> itemService.addComment(commentInputDto));
        assertEquals("Item not found", oe.getMessage());

        commentInputDto.setItemId(item.getId());
        ValidationException ve = assertThrows(ValidationException.class,
                () -> itemService.addComment(commentInputDto));
        assertEquals("User hasn`t booked this item", ve.getMessage());

        commentInputDto.setAuthorId(ownerId);
        ve = assertThrows(ValidationException.class,
                () -> itemService.addComment(commentInputDto));
        assertEquals("Owner cannot leave comments", ve.getMessage());

    }

    @Test
    void testAddComment() {


        ItemInputDto item = ItemInputDto.builder()
                .name("item")
                .description("description")
                .available(true)
                .build();

        UserInputDto owner = UserInputDto.builder()
                .name("owner")
                .email("owner@user.com")
                .build();
        Long ownerId = userService.createUser(owner).getId();
        UserInputDto booker1 = UserInputDto.builder()
                .name("booker1")
                .email("booker1@user.com")
                .build();
        Long booker1Id = userService.createUser(booker1).getId();

        item.setId(itemService.addItem(item, ownerId).getId());

        CommentInputDto commentInputDto = CommentInputDto.builder()
                .text("comment")
                .authorId(booker1Id)
                .itemId(item.getId())
                .build();

        BookingDto bookingDto = bookingService.addBooking(BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .build(), booker1Id);

        bookingService.setApprovedStatus(bookingDto.getId(), ownerId, true);

        CommentDto expectedCommentDto = CommentDto.builder()
                .authorName(booker1.getName())
                .text(commentInputDto.getText())
                .build();

        CommentDto createCommentDto = itemService.addComment(commentInputDto);
        expectedCommentDto.setId(createCommentDto.getId());
        expectedCommentDto.setCreated(createCommentDto.getCreated());

        CommentDto getCommentDto = itemService.getItemById(item.getId(), ownerId).getComments().get(0);
        assertEquals(expectedCommentDto, createCommentDto);
        assertEquals(expectedCommentDto, getCommentDto);


        ObjectAlreadyExists ae = assertThrows(ObjectAlreadyExists.class,
                () -> itemService.addComment(commentInputDto));
        assertEquals("Author is allowed to leave one comment", ae.getMessage());
    }

    @Test
    void getAllItemsWithComments() {


        ItemInputDto item1 = ItemInputDto.builder()
                .name("item1")
                .description("description")
                .available(true)
                .build();

        ItemInputDto item2 = ItemInputDto.builder()
                .name("item1")
                .description("description")
                .available(false)
                .build();

        UserInputDto owner = UserInputDto.builder()
                .name("owner")
                .email("owner@user.com")
                .build();
        Long ownerId = userService.createUser(owner).getId();
        UserInputDto booker1 = UserInputDto.builder()
                .name("booker1")
                .email("booker1@user.com")
                .build();
        Long booker1Id = userService.createUser(booker1).getId();

        UserInputDto booker2 = UserInputDto.builder()
                .name("booker2")
                .email("booker2@user.com")
                .build();
        Long booker2Id = userService.createUser(booker2).getId();

        item1.setId(itemService.addItem(item1, ownerId).getId());
        item2.setId(itemService.addItem(item2, ownerId).getId());

        CommentInputDto commentInputDto = CommentInputDto.builder()
                .text("comment")
                .authorId(booker1Id)
                .itemId(item1.getId())
                .build();

        BookingDto bookingDto1 = bookingService.addBooking(BookingInputDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().minusHours(2))
                .end(LocalDateTime.now().minusHours(1))
                .build(), booker1Id);

        BookingDto bookingDto2 = bookingService.addBooking(BookingInputDto.builder()
                .itemId(item1.getId())
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().plusHours(2))
                .build(), booker2Id);


        bookingService.setApprovedStatus(bookingDto1.getId(), ownerId, true);
        bookingService.setApprovedStatus(bookingDto2.getId(), ownerId, true);

        CommentDto createCommentDto = itemService.addComment(commentInputDto);

        ItemDto item1Dto = ItemDto.builder()
                .id(item1.getId())
                .name(item1.getName())
                .available(item1.getAvailable())
                .description(item1.getDescription())
                .comments(new ArrayList<>(List.of(createCommentDto)))
                .lastBooking(BookerDtoInItem.builder()
                        .bookerId(booker1Id)
                        .id(bookingDto1.getId())
                        .build())
                .nextBooking(BookerDtoInItem.builder()
                        .bookerId(booker2Id)
                        .id(bookingDto2.getId())
                        .build())
                .build();

        ItemDto item2Dto = ItemDto.builder()
                .id(item2.getId())
                .name(item2.getName())
                .available(item2.getAvailable())
                .description(item2.getDescription())
                .comments(new ArrayList<>())
                .build();

        ArrayList<ItemDto> itemDtos = (ArrayList<ItemDto>) itemService.getAllItemsOfUser(ownerId, 0, 10);
        assertEquals(2, itemDtos.size());
        assertEquals(item1Dto, itemDtos.get(0));
        assertEquals(item2Dto, itemDtos.get(1));

    }

    @Test
    void testSearch() {

        UserInputDto owner = UserInputDto.builder()
                .name("owner")
                .email("owner@user.com")
                .build();
        Long ownerId = userService.createUser(owner).getId();

        ItemInputDto item1 = ItemInputDto.builder()
                .name("name")
                .description("iteM description")
                .available(true)
                .build();

        ItemInputDto item2 = ItemInputDto.builder()
                .name("ItEm2")
                .description("description")
                .available(true)
                .build();
        ItemInputDto item3 = ItemInputDto.builder()
                .name("ItEm3")
                .description("item")
                .available(false)
                .build();

        item1.setId(itemService.addItem(item1, ownerId).getId());
        item2.setId(itemService.addItem(item2, ownerId).getId());
        item3.setId(itemService.addItem(item3, ownerId).getId());


        ArrayList<ItemDto> searchList = (ArrayList<ItemDto>) itemService.searchItem("ITEM", 0, 10);
        assertEquals(2, searchList.size());
        assertEquals(item1.getId(), searchList.get(0).getId());
        assertEquals(item2.getId(), searchList.get(1).getId());

    }

}
