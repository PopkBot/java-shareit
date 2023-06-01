package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;


@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "text")
    private String text;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User author;
    @Column(name = "creation_date")
    private Long created;
    @Column(name = "item_id")
    private Long itemId;


}
