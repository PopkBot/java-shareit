package ru.practicum.shareit.item.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
public class Item {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long userId;

}
