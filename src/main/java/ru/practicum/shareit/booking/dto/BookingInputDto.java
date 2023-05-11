package ru.practicum.shareit.booking.dto;

import lombok.Data;

import java.util.Date;

@Data
public class BookingInputDto {

    private Long itemId;
    private Date start;
    private Date end;

}
