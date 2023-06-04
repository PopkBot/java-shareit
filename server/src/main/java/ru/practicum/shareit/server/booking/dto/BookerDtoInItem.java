package ru.practicum.shareit.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookerDtoInItem {

    private Long id;
    private Long bookerId;

}
