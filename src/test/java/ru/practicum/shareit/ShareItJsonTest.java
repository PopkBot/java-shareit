package ru.practicum.shareit;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class ShareItJsonTest {

    @Autowired
    private JacksonTester<ItemRequestDto> itemRDJson;
    @Autowired
    private JacksonTester<CommentDto> commentDtoJson;
    @Autowired
    private JacksonTester<BookingDto> bookingDtoJson;

    private final DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Test
    void testItemRequestDto() throws IOException {

        LocalDateTime now = LocalDateTime.now();

        ItemRequestDto itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .created(now)
                .description("description")
                .items(List.of(Item.builder()
                        .name("item")
                        .id(1L)
                        .available(true)
                        .user(
                                User.builder()
                                .id(2L)
                                .name("user")
                                .email("user@email.com")
                                .build())
                        .build()))
                .build();

        JsonContent<ItemRequestDto> result = itemRDJson.write(itemRequestDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
       assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(now.format(dTF));
        assertThat(result).extractingJsonPathStringValue("$.description")
                .isEqualTo("description");
        assertThat(result).extractingJsonPathStringValue("$.items[0].name")
                .isEqualTo("item");
        assertThat(result).extractingJsonPathNumberValue("$.items[0].id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathBooleanValue("$.items[0].available")
                .isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.items[0].user.id")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.items[0].user.name")
                .isEqualTo("user");
    }

    @Test
    void testCommentDtoJson() throws IOException {

        LocalDateTime now = LocalDateTime.now();

        CommentDto commentDto = CommentDto.builder()
                .authorName("name")
                .text("text")
                .created(now)
                .id(1L)
                .build();


        JsonContent<CommentDto> result = commentDtoJson.write(commentDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.text")
                .isEqualTo("text");
        assertThat(result).extractingJsonPathStringValue("$.authorName")
                .isEqualTo("name");
        assertThat(result).extractingJsonPathStringValue("$.created")
                .isEqualTo(now.format(dTF));

    }

    @Test
    void testBookingDtoJson() throws IOException {

        Date now = Date.from(Instant.now());
        LocalDateTime ldNow = LocalDateTime.now();

        BookingDto bookingDto = BookingDto.builder()
                .id(1L)
                .start(now)
                .end(now)
                .booker(
                        UserDto.builder()
                        .id(2L)
                        .name("booker")
                        .email("b@mail.com")
                        .build())
                .item(
                        ItemDto.builder()
                                .id(3L)
                                .description("item description")
                                .name("item")
                                .available(true)
                                .comments(List.of(
                                                CommentDto.builder()
                                        .id(4L)
                                        .text("comment")
                                        .authorName("author")
                                        .created(ldNow)
                                        .build()))
                                .nextBooking(
                                        BookerDtoInItem.builder()
                                        .id(1L)
                                        .bookerId(2L)
                                        .build())
                                .build()
                )
                .build();


        JsonContent<BookingDto> result = bookingDtoJson.write(bookingDto);
        assertThat(result).extractingJsonPathNumberValue("$.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathStringValue("$.start")
                .isEqualTo(LocalDateTime.ofInstant(now.toInstant(), ZoneId.of("UTC")).format(dTF));
        assertThat(result).extractingJsonPathStringValue("$.end")
                .isEqualTo(LocalDateTime.ofInstant(now.toInstant(), ZoneId.of("UTC")).format(dTF));
        assertThat(result).extractingJsonPathNumberValue("$.booker.id")
                .isEqualTo(2);
        assertThat(result).extractingJsonPathStringValue("$.booker.name")
                .isEqualTo("booker");
        assertThat(result).extractingJsonPathStringValue("$.booker.email")
                .isEqualTo("b@mail.com");
        assertThat(result).extractingJsonPathStringValue("$.item.description")
                .isEqualTo("item description");
        assertThat(result).extractingJsonPathNumberValue("$.item.id")
                .isEqualTo(3);
        assertThat(result).extractingJsonPathStringValue("$.item.name")
                .isEqualTo("item");
        assertThat(result).extractingJsonPathBooleanValue("$.item.available")
                .isEqualTo(true);
        assertThat(result).extractingJsonPathNumberValue("$.item.comments[0].id")
                .isEqualTo(4);
        assertThat(result).extractingJsonPathStringValue("$.item.comments[0].text")
                .isEqualTo("comment");
        assertThat(result).extractingJsonPathStringValue("$.item.comments[0].created")
                .isEqualTo(ldNow.format(dTF));
        assertThat(result).extractingJsonPathNumberValue("$.item.nextBooking.id")
                .isEqualTo(1);
        assertThat(result).extractingJsonPathNumberValue("$.item.nextBooking.bookerId")
                .isEqualTo(2);

    }
}
