package ru.practicum.shareit.server.booking.service;


import ru.practicum.shareit.server.booking.dto.BookingRequestParamsDto;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingInputDto;

import java.util.List;

public interface BookingService {

    BookingDto getBookingById(Long id, Long userId);

    BookingDto addBooking(BookingInputDto bookingInputDto, Long bookerId);

    BookingDto setApprovedStatus(Long bookingId, Long ownerId, boolean isApproved);

    List<BookingDto> getBookingsOfUser(BookingRequestParamsDto bookingRequestParamsDto);
}
