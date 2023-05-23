package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingRequestParamsDto {

    private Long ownerId;
    private String statusString;
    private String userType;
    private Integer from;
    private Integer size;

}
