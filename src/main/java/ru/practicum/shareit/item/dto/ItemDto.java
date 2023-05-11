package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;
import ru.practicum.shareit.booking.dto.BookingDto;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private BookerDtoInItem nextBooking;
    private BookerDtoInItem lastBooking;
}
