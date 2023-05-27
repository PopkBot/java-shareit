package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Instant;
import java.util.List;

@DataJpaTest
public class BookingRepositoryTest {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;


    @Test
    void testGetAllBookingsOfOwner(){

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .user(owner)
                .build();

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking booking1 = Booking.builder()
                .start(now)
                .end(now)
                .owner(owner)
                .booker(booker)
                .status(Status.WAITING)
                .item(item)
                .build();

        Booking booking2 = Booking.builder()
                .start(now.minusSeconds(10))
                .end(now.minusSeconds(10))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());

        List<Booking> foundBookings = bookingRepository.getAllBookingsOfOwner(owner.getId(),"owner",0,10);
        assertEquals(2,foundBookings.size());
        assertEquals(booking1,foundBookings.get(0));
        assertEquals(booking2,foundBookings.get(1));


    }

    @Test
    void testCountOverlaps(){

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .user(owner)
                .build();

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking booking1 = Booking.builder()
                .start(now.plusSeconds(10))
                .end(now.plusSeconds(20))
                .owner(owner)
                .booker(booker)
                .status(Status.WAITING)
                .item(item)
                .build();

        Booking booking2 = Booking.builder()
                .start(now.minusSeconds(20))
                .end(now.minusSeconds(10))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking3 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.minusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        String start = now.minusSeconds(15).toString();
        String end = now.plusSeconds(15).toString();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());
        assertNotNull(booking3.getId());

        Long count = bookingRepository.countDateOverlaps(start,end,item.getId());
        assertNotNull(count);
        assertEquals(1L,count);

    }

    @Test
    void testGetBookingsByStatus(){

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .user(owner)
                .build();

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking booking1 = Booking.builder()
                .start(now.plusSeconds(10))
                .end(now.plusSeconds(20))
                .owner(owner)
                .booker(booker)
                .status(Status.WAITING)
                .item(item)
                .build();

        Booking booking2 = Booking.builder()
                .start(now.minusSeconds(20))
                .end(now.minusSeconds(10))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking3 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.minusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.REJECTED)
                .item(item)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());
        assertNotNull(booking3.getId());




        List<Booking> approvedBookings = bookingRepository.getBookingsOfOwnerByApproval(owner.getId(),
                "APPROVED","owner",0,10);
        List<Booking> waitingBookings = bookingRepository.getBookingsOfOwnerByApproval(owner.getId(),
                "WAITING","owner",0,10);
        List<Booking> rejectedBookings = bookingRepository.getBookingsOfOwnerByApproval(owner.getId(),
                "REJECTED","owner",0,10);
        assertNotNull(approvedBookings);
        assertNotNull(waitingBookings);
        assertNotNull(rejectedBookings);

        assertEquals(1,approvedBookings.size());
        assertEquals(1,waitingBookings.size());
        assertEquals(1,rejectedBookings.size());

        assertEquals(booking2,approvedBookings.get(0));
        assertEquals(booking1,waitingBookings.get(0));
        assertEquals(booking3,rejectedBookings.get(0));

    }

    @Test
    void testGetBookingsByTime(){

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .user(owner)
                .build();

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking booking1 = Booking.builder()
                .start(now.plusSeconds(10))
                .end(now.plusSeconds(20))
                .owner(owner)
                .booker(booker)
                .status(Status.WAITING)
                .item(item)
                .build();

        Booking booking2 = Booking.builder()
                .start(now.minusSeconds(20))
                .end(now.minusSeconds(10))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking3 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.plusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.REJECTED)
                .item(item)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());
        assertNotNull(booking3.getId());




        List<Booking> pastBookings = bookingRepository.getPastBookingsOfOwner(owner.getId(),
                now.toString(),"owner",0,10);
        List<Booking> currentBookings = bookingRepository.getCurrentBookingsOfOwner(booker.getId(),
                now.toString(),"booker",0,10);
        List<Booking> futureBookings = bookingRepository.getFutureBookingsOfOwner(owner.getId(),
                now.toString(),"owner",0,10);
        assertNotNull(pastBookings);
        assertNotNull(currentBookings);
        assertNotNull(futureBookings);

        assertEquals(1,pastBookings.size());
        assertEquals(1,currentBookings.size());
        assertEquals(1,futureBookings.size());

        assertEquals(booking2,pastBookings.get(0));
        assertEquals(booking1,futureBookings.get(0));
        assertEquals(booking3,currentBookings.get(0));

    }

    @Test
    void testCountByBookerItemStatus(){

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .user(owner)
                .build();

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking booking1 = Booking.builder()
                .start(now.plusSeconds(10))
                .end(now.plusSeconds(20))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking2 = Booking.builder()
                .start(now.minusSeconds(20))
                .end(now.minusSeconds(10))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking3 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.plusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking4 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.plusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.REJECTED)
                .item(item)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());
        assertNotNull(booking3.getId());
        assertNotNull(booking4.getId());

        Long count = bookingRepository.countByBookerIdAndItemIdAndStatus(booker.getId(),item.getId(),
                "APPROVED",now.toString());
        assertNotNull(count);
        assertEquals(2L,count);

    }

    @Test
    void testLastAndNextBooking(){

        User booker = User.builder()
                .name("booker")
                .email("booker@mail.com")
                .build();
        User owner = User.builder()
                .name("owner")
                .email("user@mail.com")
                .build();
        Item item = Item.builder()
                .name("item")
                .description("desc")
                .available(true)
                .user(owner)
                .build();

        userRepository.save(booker);
        userRepository.save(owner);
        itemRepository.save(item);

        Instant now = Instant.now();

        Booking booking1 = Booking.builder()
                .start(now.plusSeconds(10))
                .end(now.plusSeconds(20))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking2 = Booking.builder()
                .start(now.minusSeconds(20))
                .end(now.minusSeconds(10))
                .owner(owner)
                .booker(booker)
                .status(Status.REJECTED)
                .item(item)
                .build();

        Booking booking3 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.plusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.APPROVED)
                .item(item)
                .build();

        Booking booking4 = Booking.builder()
                .start(now.minusSeconds(50))
                .end(now.plusSeconds(40))
                .owner(owner)
                .booker(booker)
                .status(Status.REJECTED)
                .item(item)
                .build();

        bookingRepository.save(booking1);
        bookingRepository.save(booking2);
        bookingRepository.save(booking3);
        bookingRepository.save(booking4);
        assertNotNull(booking1.getId());
        assertNotNull(booking2.getId());
        assertNotNull(booking3.getId());
        assertNotNull(booking4.getId());

        Booking nextBooking = bookingRepository.getNextBooking(now.toString(),item.getId());
        Booking lastBooking = bookingRepository.getLastBooking(now.toString(),item.getId());

        assertNotNull(nextBooking);
        assertNotNull(lastBooking);
        assertEquals(booking1,nextBooking);
        assertEquals(booking3,lastBooking);

    }

}
