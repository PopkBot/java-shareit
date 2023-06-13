package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.gateway.booking.dto.BookerDtoInItem;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private boolean available;
    private BookerDtoInItem nextBooking;
    private BookerDtoInItem lastBooking;
    private List<CommentDto> comments;
    private Long requestId;

}
