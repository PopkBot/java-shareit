package ru.practicum.shareit.booking.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingRequestParamsDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.validation.BookingCreate;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {

    private final BookingService bookingService;

    @PostMapping("/bookings")
    public BookingDto addBooking(@BookingCreate @RequestBody BookingInputDto bookingInputDto,
                                 @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        log.info("Booking {} is requested", bookingInputDto);
        return bookingService.addBooking(bookingInputDto, bookerId);
    }

    @PatchMapping("bookings/{bookingId}")
    public BookingDto setApproveStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                       @PathVariable Long bookingId,
                                       @RequestParam boolean approved) {
        log.info("Booking {} set approved to {}", bookingId, approved);
        return bookingService.setApprovedStatus(bookingId, ownerId, approved);
    }

    @GetMapping("/bookings/owner")
    public List<BookingDto> getBookingsOfOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                               @RequestParam(required = false, defaultValue = "ALL") String state,
                                               @RequestParam(required = false,defaultValue = "0")Integer from,
                                               @RequestParam(required = false,defaultValue = "10")Integer size) {
        log.info("All bookings of owner {}, status {}", ownerId, state);
        BookingRequestParamsDto bookingRequestParamsDto = BookingRequestParamsDto.builder()
                .ownerId(ownerId)
                .statusString(state)
                .userType("owner")
                .from(from)
                .size(size)
                .build();
        return bookingService.getBookingsOfUser(bookingRequestParamsDto);
    }

    @GetMapping("/bookings/{bookingId}")
    public BookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                     @PathVariable Long bookingId) {
        log.info("Booking of user {} is requested", userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @GetMapping("/bookings")
    public List<BookingDto> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                              @RequestParam(required = false, defaultValue = "ALL") String state,
                                              @RequestParam(required = false,defaultValue = "0")Integer from,
                                              @RequestParam(required = false,defaultValue = "10")Integer size) {
        log.info("All bookings of user {}, status {}", ownerId, state);
        BookingRequestParamsDto bookingRequestParamsDto = BookingRequestParamsDto.builder()
                .ownerId(ownerId)
                .statusString(state)
                .userType("booker")
                .from(from)
                .size(size)
                .build();
        return bookingService.getBookingsOfUser(bookingRequestParamsDto);
    }


}
