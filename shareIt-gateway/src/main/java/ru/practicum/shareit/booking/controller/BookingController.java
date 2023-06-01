package ru.practicum.shareit.booking.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.client.BookingClient;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingRequestParamsDto;
import ru.practicum.shareit.booking.validation.BookingCreate;

@RestController
@RequiredArgsConstructor
@Validated
public class BookingController {

    private final BookingClient baseClient;

    @PostMapping("/bookings")
    public ResponseEntity<Object> addBooking(@BookingCreate @RequestBody BookingInputDto bookingInputDto,
                                             @RequestHeader("X-Sharer-User-Id") Long bookerId) {
        return baseClient.addBooking(bookingInputDto, bookerId);
    }

    @PatchMapping("bookings/{bookingId}")
    public ResponseEntity<Object> setApproveStatus(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                   @PathVariable Long bookingId,
                                                   @RequestParam boolean approved) {
        return baseClient.setApproveStatus(bookingId, ownerId, approved);
    }

    @GetMapping("/bookings/owner")
    public ResponseEntity<Object> getBookingsOfOwner(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                     @RequestParam(required = false, defaultValue = "ALL") String state,
                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        BookingRequestParamsDto bookingRequestParamsDto = BookingRequestParamsDto.builder()
                .ownerId(ownerId)
                .statusString(state)
                .userType("owner")
                .from(from)
                .size(size)
                .build();
        return baseClient.getBookingsOfUser(bookingRequestParamsDto);
    }

    @GetMapping("/bookings/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                 @PathVariable Long bookingId) {
        return baseClient.getBookingById(bookingId, userId);
    }

    @GetMapping("/bookings")
    public ResponseEntity<Object> getBookingsOfUser(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                    @RequestParam(required = false, defaultValue = "ALL") String state,
                                                    @RequestParam(required = false, defaultValue = "0") Integer from,
                                                    @RequestParam(required = false, defaultValue = "10") Integer size) {
        BookingRequestParamsDto bookingRequestParamsDto = BookingRequestParamsDto.builder()
                .ownerId(ownerId)
                .statusString(state)
                .userType("booker")
                .from(from)
                .size(size)
                .build();
        return baseClient.getBookingsOfUser(bookingRequestParamsDto);
    }


}
