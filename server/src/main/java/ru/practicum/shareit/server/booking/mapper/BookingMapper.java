package ru.practicum.shareit.server.booking.mapper;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.convention.NameTokenizers;
import org.modelmapper.convention.NamingConventions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.server.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.dto.BookingInputDto;
import ru.practicum.shareit.server.booking.model.Booking;

import java.time.ZoneId;


@Component
public class BookingMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public BookingMapper() {
        this.modelMapper = new ModelMapper();
        Configuration configuration = modelMapper.getConfiguration();
        configuration.setFieldAccessLevel(Configuration.AccessLevel.PUBLIC);
        configuration.setSourceNamingConvention(NamingConventions.JAVABEANS_ACCESSOR);
        configuration.setDestinationNamingConvention(NamingConventions.JAVABEANS_MUTATOR);
        configuration.setSourceNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setDestinationNameTokenizer(NameTokenizers.CAMEL_CASE);
        configuration.setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public BookingDto convertToBookingDto(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookingDto bookingDto = modelMapper.map(booking, BookingDto.class);
        bookingDto.setStart(booking.getStart().toLocalDateTime());
        bookingDto.setEnd(booking.getEnd().toLocalDateTime());
        return bookingDto;
    }

    public Booking convertToBooking(BookingDto bookingDto) {
        return modelMapper.map(bookingDto, Booking.class);
    }

    public Booking convertToBooking(BookingInputDto bookingInputDto) {
        Booking booking = modelMapper.map(bookingInputDto, Booking.class);
        booking.setStart(bookingInputDto.getStart().atZone(ZoneId.systemDefault()));
        booking.setEnd(bookingInputDto.getEnd().atZone(ZoneId.systemDefault()));
        return booking;
    }

    public BookerDtoInItem convertToBookingDtoInItem(Booking booking) {
        if (booking == null) {
            return null;
        }
        BookerDtoInItem bookerDtoInItem = modelMapper.map(booking, BookerDtoInItem.class);
        bookerDtoInItem.setBookerId(booking.getBooker().getId());
        return bookerDtoInItem;
    }
}
