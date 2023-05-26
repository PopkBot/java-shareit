package ru.practicum.shareit.booking;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingRequestParamsDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImpTest {

    private final BookingService bookingService;
    private final UserService userService;
    private final ItemService itemService;

    @Test
    void testAddBooking() {


        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .itemId(-1L)
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();
        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.addBooking(bookingInputDto, -1L));
        assertEquals("Booker not found", oe.getMessage());

        User user = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        user.setId(userService.createUser(user).getId());

        oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.addBooking(bookingInputDto, user.getId()));
        assertEquals("Item not found", oe.getMessage());

        Item item = Item.builder()
                .name("item")
                .description("description")
                .available(false)
                .build();
        item.setId(itemService.addItem(item, user.getId()).getId());

        bookingInputDto.setItemId(item.getId());

        oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.addBooking(bookingInputDto, user.getId()));
        assertEquals("Owner cannot book own item", oe.getMessage());

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        booker.setId(userService.createUser(booker).getId());

        ValidationException ve = assertThrows(ValidationException.class,
                () -> bookingService.addBooking(bookingInputDto, booker.getId()));
        assertEquals("Item is not available", ve.getMessage());

        item.setAvailable(true);
        itemService.updateItem(item, user.getId());

        BookingDto bookingDto = bookingService.addBooking(bookingInputDto, booker.getId());
        assertEquals(bookingInputDto.getItemId(), bookingDto.getItem().getId());
        assertEquals(booker.getId(), bookingDto.getBooker().getId());
        DateTimeFormatter dTF = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        assertEquals(bookingInputDto.getEnd().format(dTF),
                bookingDto.getEnd().toInstant().atZone(ZoneOffset.ofHours(0)).format(dTF));
        assertEquals(bookingInputDto.getStart().format(dTF),
                bookingDto.getStart().toInstant().atZone(ZoneOffset.ofHours(0)).format(dTF));

    }

    @Test
    void testSetApproved() {

        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.setApprovedStatus(-1L, -1L, true));
        assertEquals("Booking not found", oe.getMessage());

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        booker.setId(userService.createUser(booker).getId());
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        owner.setId(userService.createUser(owner).getId());
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();
        item.setId(itemService.addItem(item, owner.getId()).getId());
        BookingInputDto bookingIDto = BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusMinutes(2))
                .end(LocalDateTime.now().plusHours(10))
                .build();

        BookingDto bookingDto = bookingService.addBooking(bookingIDto, booker.getId());

        oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.setApprovedStatus(bookingDto.getId(), booker.getId(), false));
        assertEquals("This user is not the owner", oe.getMessage());

        bookingService.setApprovedStatus(bookingDto.getId(), owner.getId(), true);
        ValidationException ve = assertThrows(ValidationException.class,
                () -> bookingService.setApprovedStatus(bookingDto.getId(), owner.getId(), true));
        assertEquals("Cannot set same status", ve.getMessage());

        BookingInputDto bookingIDto2 = BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(5))
                .build();
        BookingDto bookingDto2 = bookingService.addBooking(bookingIDto2, booker.getId());
        bookingService.setApprovedStatus(bookingDto2.getId(), owner.getId(), true);
        ve = assertThrows(ValidationException.class,
                () -> bookingService.setApprovedStatus(bookingDto2.getId(), owner.getId(), false));
        assertEquals("Cannot change completed booking", ve.getMessage());

        BookingDto bookingDtoRejected = bookingService.setApprovedStatus(bookingDto.getId(), owner.getId(), false);
        assertEquals(Status.REJECTED, bookingDtoRejected.getStatus());


    }

    @Test
    void testGetBookingsOfUser() {

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        booker.setId(userService.createUser(booker).getId());
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        owner.setId(userService.createUser(owner).getId());
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();
        item.setId(itemService.addItem(item, owner.getId()).getId());

        BookingRequestParamsDto bookingRPD = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("NOT STATUS")
                .ownerId(-1L)
                .from(-10)
                .size(-10)
                .build();


        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingsOfUser(bookingRPD));
        assertEquals("User not found", oe.getMessage());

        bookingRPD.setOwnerId(owner.getId());

        ValidationException ve = assertThrows(ValidationException.class,
                () -> bookingService.getBookingsOfUser(bookingRPD));
        assertEquals("Unknown state: NOT STATUS", ve.getMessage());

        bookingRPD.setStatusString(Status.APPROVED.toString());

        ve = assertThrows(ValidationException.class,
                () -> bookingService.getBookingsOfUser(bookingRPD));
        assertEquals("invalid page parameters", ve.getMessage());

        BookingInputDto bookingID1 = BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().minusHours(5))
                .build();
        BookingInputDto bookingID2 = BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().minusHours(10))
                .end(LocalDateTime.now().plusHours(10))
                .build();
        BookingInputDto bookingID3 = BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now().plusHours(20))
                .end(LocalDateTime.now().plusHours(30))
                .build();

        BookingDto bookingDto1 = bookingService.addBooking(bookingID1, booker.getId());
        BookingDto bookingDto2 = bookingService.addBooking(bookingID2, booker.getId());
        BookingDto bookingDto3 = bookingService.addBooking(bookingID3, booker.getId());

        BookingRequestParamsDto bookingRPDWaiting = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("WAITING")
                .ownerId(owner.getId())
                .from(0)
                .size(10)
                .build();

        List<BookingDto> waitingBookings = bookingService.getBookingsOfUser(bookingRPDWaiting);
        assertEquals(3, waitingBookings.size());

        bookingDto1 = bookingService.setApprovedStatus(bookingDto1.getId(), owner.getId(), true);
        bookingDto2 = bookingService.setApprovedStatus(bookingDto2.getId(), owner.getId(), true);
        bookingDto3 = bookingService.setApprovedStatus(bookingDto3.getId(), owner.getId(), false);

        BookingRequestParamsDto bookingRPDApproved = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("APPROVED")
                .ownerId(owner.getId())
                .from(0)
                .size(10)
                .build();
        List<BookingDto> approvedBookings = bookingService.getBookingsOfUser(bookingRPDApproved);
        assertEquals(2, approvedBookings.size());

        BookingRequestParamsDto bookingRPDRejected = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("REJECTED")
                .ownerId(owner.getId())
                .from(0)
                .size(10)
                .build();
        List<BookingDto> rejectedBookings = bookingService.getBookingsOfUser(bookingRPDRejected);
        assertEquals(1, rejectedBookings.size());

        bookingDto3 = bookingService.setApprovedStatus(bookingDto3.getId(), owner.getId(), true);
        BookingRequestParamsDto bookingRPDPast = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("PAST")
                .ownerId(owner.getId())
                .from(0)
                .size(10)
                .build();
        List<BookingDto> pastBookings = bookingService.getBookingsOfUser(bookingRPDPast);
        assertEquals(bookingDto1.getId(), pastBookings.get(0).getId());

        BookingRequestParamsDto bookingRPDCurrent = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("CUrrent")
                .ownerId(owner.getId())
                .from(0)
                .size(10)
                .build();
        List<BookingDto> currentBookings = bookingService.getBookingsOfUser(bookingRPDCurrent);
        assertEquals(bookingDto2.getId(), currentBookings.get(0).getId());

        BookingRequestParamsDto bookingRPDFuture = BookingRequestParamsDto.builder()
                .userType("owner")
                .statusString("future")
                .ownerId(owner.getId())
                .from(0)
                .size(10)
                .build();
        List<BookingDto> futureBookings = bookingService.getBookingsOfUser(bookingRPDFuture);
        assertEquals(bookingDto3.getId(), futureBookings.get(0).getId());


    }

    @Test
    void testGetBookingById() {

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        booker.setId(userService.createUser(booker).getId());
        User owner = User.builder()
                .name("owner")
                .email("owner@mail.com")
                .build();
        owner.setId(userService.createUser(owner).getId());
        User user = User.builder()
                .name("user")
                .email("user@mail.com")
                .build();
        user.setId(userService.createUser(user).getId());
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .build();
        item.setId(itemService.addItem(item, owner.getId()).getId());

        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingById(-1L, owner.getId()));
        assertEquals("Booking not found", oe.getMessage());

        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .itemId(item.getId())
                .start(LocalDateTime.now())
                .end(LocalDateTime.now())
                .build();

        BookingDto createdBooking = bookingService.addBooking(bookingInputDto, booker.getId());

        oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingById(createdBooking.getId(), -1L));
        assertEquals("User not found", oe.getMessage());

        oe = assertThrows(ObjectNotFoundException.class,
                () -> bookingService.getBookingById(createdBooking.getId(), user.getId()));
        assertEquals("Access denied", oe.getMessage());

        BookingDto returnedBookingDto = bookingService.getBookingById(createdBooking.getId(), booker.getId());
        assertEquals(createdBooking, returnedBookingDto);


    }
}
