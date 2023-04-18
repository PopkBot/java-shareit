package ru.practicum.shareit.item.model;

import lombok.*;
import org.springframework.lang.Nullable;
import ru.practicum.shareit.item.validation.ItemCreate;
import ru.practicum.shareit.item.validation.ItemUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
