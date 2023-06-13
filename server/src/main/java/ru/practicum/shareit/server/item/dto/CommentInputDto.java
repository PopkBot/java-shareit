package ru.practicum.shareit.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentInputDto {

    private String text;
    private Long itemId;
    private Long authorId;

}
