package ru.practicum.shareit.server.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.server.booking.Status;
import ru.practicum.shareit.server.booking.dto.BookingRequestParamsDto;
import ru.practicum.shareit.server.booking.mapper.BookingMapper;
import ru.practicum.shareit.server.booking.model.Booking;
import ru.practicum.shareit.server.booking.repository.BookingRepository;
import ru.practicum.shareit.server.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.server.exceptions.ValidationException;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.repository.ItemRepository;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingInputDto;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

        /*
        Long itemId = booking.getItem().getId();
        Long overlaps = bookingRepository.countDateOverlaps(
                Timestamp.from(booking.getStart()).toString()+"Z",
                Timestamp.from(booking.getEnd()).toString()+"Z",
                itemId);
        if (overlaps > 0) {
            throw new ValidationException("Cannot approve overlap bookings");
        }*/
        LocalDateTime end = LocalDateTime.ofInstant(booking.getEnd(), ZoneId.of("UTC"));
        LocalDateTime now = LocalDateTime.now();
        if (isApproved) {
            booking.setStatus(Status.APPROVED);
        } else if (end.isBefore(now)
                && booking.getStatus().equals(Status.APPROVED)) {
            throw new ValidationException("Cannot change completed booking");
        } else {
            booking.setStatus(Status.REJECTED);
        }
        log.info("Booking {} status is updated to {}", bookingId, booking.getStatus());
        return bookingMapper.convertToBookingDto(bookingRepository.save(booking));
    }

    @Override
    public List<BookingDto> getBookingsOfUser(BookingRequestParamsDto paramsDto) {

        userRepository.findById(paramsDto.getOwnerId()).orElseThrow(
                () -> new ObjectNotFoundException("User not found")
        );
        paramsDto.setStatusString(paramsDto.getStatusString().toUpperCase());
        if (Arrays.stream(Status.values()).noneMatch(status -> status.toString().equals(paramsDto.getStatusString()))) {
            throw new ValidationException("Unknown state: " + paramsDto.getStatusString());
        }
        if (paramsDto.getSize() <= 0 || paramsDto.getFrom() < 0) {
            throw new ValidationException("invalid page parameters");
        }

        Status status = Status.valueOf(paramsDto.getStatusString());
        List<Booking> bookings;
        String nowStr = Timestamp.from(Instant.now()) + "Z";
        switch (status) {
            case ALL:
                bookings = bookingRepository.getAllBookingsOfOwner(paramsDto.getOwnerId(), paramsDto.getUserType(),
                        paramsDto.getFrom(), paramsDto.getSize());
                break;
            case APPROVED:
                bookings = bookingRepository
                        .getBookingsOfOwnerByApproval(paramsDto.getOwnerId(), Status.APPROVED.toString(),
                                paramsDto.getUserType(), paramsDto.getFrom(), paramsDto.getSize());
                break;
            case REJECTED:
                bookings = bookingRepository
                        .getBookingsOfOwnerByApproval(paramsDto.getOwnerId(), Status.REJECTED.toString(),
                                paramsDto.getUserType(), paramsDto.getFrom(), paramsDto.getSize());
                break;
            case WAITING:
                bookings = bookingRepository
                        .getBookingsOfOwnerByApproval(paramsDto.getOwnerId(), Status.WAITING.toString(),
                                paramsDto.getUserType(), paramsDto.getFrom(), paramsDto.getSize());
                break;
            case FUTURE:
                bookings = bookingRepository.getFutureBookingsOfOwner(paramsDto.getOwnerId(), nowStr,
                        paramsDto.getUserType(), paramsDto.getFrom(), paramsDto.getSize());
                break;
            case CURRENT:
                bookings = bookingRepository.getCurrentBookingsOfOwner(paramsDto.getOwnerId(), nowStr,
                        paramsDto.getUserType(), paramsDto.getFrom(), paramsDto.getSize());
                break;
            case PAST:
                bookings = bookingRepository.getPastBookingsOfOwner(paramsDto.getOwnerId(), nowStr,
                        paramsDto.getUserType(), paramsDto.getFrom(), paramsDto.getSize());
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
