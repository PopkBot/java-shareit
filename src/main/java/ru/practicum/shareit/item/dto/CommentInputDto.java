package ru.practicum.shareit.item.dto;

import lombok.Data;

@Data
public class CommentInputDto {

    private String text;
    private Long itemId;
    private Long authorId;

}
