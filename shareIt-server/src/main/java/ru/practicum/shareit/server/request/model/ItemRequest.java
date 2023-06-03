package ru.practicum.shareit.server.request.model;

import lombok.*;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item_requests")
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "created")
    private ZonedDateTime created;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_request_id")
    private List<Item> items = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "requesting_user_id")
    private User requestingUser;

}
