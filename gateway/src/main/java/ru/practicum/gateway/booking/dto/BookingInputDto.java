package ru.practicum.gateway.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingInputDto {

    private Long itemId;
    private LocalDateTime start;
    private LocalDateTime end;

}
