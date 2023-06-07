package ru.practicum.gateway.item.model;

import lombok.*;
import ru.practicum.gateway.user.model.User;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Comment {

    private Long id;
    private String text;
    private User author;
    private Long created;
    private Long itemId;


}
