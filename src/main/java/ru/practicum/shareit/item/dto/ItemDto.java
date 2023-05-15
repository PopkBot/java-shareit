package ru.practicum.shareit.item.dto;

import lombok.Data;
import ru.practicum.shareit.booking.dto.BookerDtoInItem;

import java.util.List;

@Data
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private BookerDtoInItem nextBooking;
    private BookerDtoInItem lastBooking;
    private List<CommentDto> comments;
}
