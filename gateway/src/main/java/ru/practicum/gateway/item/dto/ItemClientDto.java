package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemClientDto {

    private Long userId;
    private Long itemId;
    private ItemInputDto itemInputDto;
    private CommentInputDto commentInputDto;
    private String searchText;
    private Integer from;
    private Integer size;
}
