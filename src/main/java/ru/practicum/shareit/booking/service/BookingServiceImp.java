package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public BookingDto getBookingById(Long id, Long userId) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Booking not found")
        );
        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("User not found")
        );
        if (!booking.getOwner().getId().equals(userId) && !booking.getBooker().getId().equals(userId)) {
            throw new ObjectNotFoundException("Access denied");
        }
        log.info("Booking {} is returned", booking);
        return bookingMapper.convertToBookingDto(booking);
    }

    @Override
    @Transactional
    public BookingDto addBooking(BookingInputDto bookingInputDto, Long bookerId) {

        User booker = userRepository.findById(bookerId).orElseThrow(
                () -> new ObjectNotFoundException("Booker not found")
        );
        Item item = itemRepository.findById(bookingInputDto.getItemId()).orElseThrow(
                () -> new ObjectNotFoundException("Item not found")
        );
        if (item.getUser().getId().equals(bookerId)) {
            throw new ObjectNotFoundException("Owner cannot book own item");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item is not available");
        }
        Booking booking = setBooking(bookingInputDto, booker, item);
        log.info("Booking {} is added", booking);
        return bookingMapper.convertToBookingDto(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingDto setApprovedStatus(Long bookingId, Long ownerId, boolean isApproved) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ObjectNotFoundException("Booking not found")
        );
        if (!booking.getOwner().getId().equals(ownerId)) {
            throw new ObjectNotFoundException("This user is not the owner");
        }
        if (booking.getStatus().equals(Status.APPROVED) && isApproved ||
                booking.getStatus().equals(Status.REJECTED) && !isApproved) {
            throw new ValidationException("Cannot set same status");
        }
        /*Long overlaps = bookingRepository.countDateOverlaps(
                Timestamp.from(booking.getStart()).toString()+"Z",
                Timestamp.from(booking.getEnd()).toString()+"Z",
                ownerId);
        if (overlaps > 0) {
            throw new ValidationException("Cannot approve overlap bookings");
        }*/
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else if (booking.getEnd().isBefore(Instant.now()) && booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Cannot change completed booking");
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.info("Booking {} status is updated to {}", bookingId, booking.getStatus());
        return bookingMapper.convertToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> getBookingsOfUser(Long userId, String statusString, String userType) {

        userRepository.findById(userId).orElseThrow(
                () -> new ObjectNotFoundException("User not found")
        );
        if (Arrays.stream(Status.values()).noneMatch(status -> status.toString().equals(statusString))) {
            throw new ValidationException("Unknown state: " + statusString);
        }
        Status status = Status.valueOf(statusString);
        List<Booking> bookings;
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        switch (status) {
            case ALL:
                bookings = bookingRepository.getAllBookingsOfOwner(userId, userType);
                break;
            case APPROVED:
                bookings = bookingRepository
                        .getBookingsOfOwnerByApproval(userId, Status.APPROVED.toString(), userType);
                break;
            case REJECTED:
                bookings = bookingRepository
                        .getBookingsOfOwnerByApproval(userId, Status.REJECTED.toString(), userType);
                break;
            case WAITING:
                bookings = bookingRepository
                        .getBookingsOfOwnerByApproval(userId, Status.WAITING.toString(), userType);
                break;
            case FUTURE:
                bookings = bookingRepository.getFutureBookingsOfOwner(userId, nowStr, userType);
                break;
            case CURRENT:
                bookings = bookingRepository.getCurrentBookingsOfOwner(userId, nowStr, userType);
                break;
            case PAST:
                bookings = bookingRepository.getPastBookingsOfOwner(userId, nowStr, userType);
                break;
            default:
                throw new ObjectNotFoundException("Unsupported status");
        }
        log.info("list of bookings returned");
        return bookings.stream().map(bookingMapper::convertToBookingDto).collect(Collectors.toList());
    }

    private Booking setBooking(BookingInputDto bookingInputDto, User booker, Item item) {
        Booking booking = bookingMapper.convertToBooking(bookingInputDto);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setOwner(item.getUser());
        booking.setStatus(Status.WAITING);
        return booking;
    }

}
