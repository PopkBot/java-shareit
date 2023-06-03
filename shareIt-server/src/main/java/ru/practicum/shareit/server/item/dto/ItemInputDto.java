package ru.practicum.shareit.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemInputDto {

    private String name;
    private String description;
    private Boolean available;
    private Long requestId;
    private Long id;


}
