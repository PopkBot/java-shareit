package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "available", nullable = false)
    @EqualsAndHashCode.Exclude
    private Boolean available;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;
    @OneToMany
    @JoinColumn(name = "item_id")
    private Set<Comment> comments = new HashSet<>();

    @Column(name = "request_Id", nullable = true)
    private Long requestId;

}
