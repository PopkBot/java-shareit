package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestParamsDto {

    private Long ownerId;
    private String statusString;
    private String userType;
    private Integer from;
    private Integer size;

}
