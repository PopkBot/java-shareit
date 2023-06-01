package ru.practicum.shareit.booking.service;


import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.dto.BookingRequestParamsDto;

import java.util.List;

public interface BookingService {

    BookingDto getBookingById(Long id, Long userId);

    BookingDto addBooking(BookingInputDto bookingInputDto, Long bookerId);

    BookingDto setApprovedStatus(Long bookingId, Long ownerId, boolean isApproved);

    List<BookingDto> getBookingsOfUser(BookingRequestParamsDto bookingRequestParamsDto);
}
