package ru.practicum.shareit.server.user.model;


import lombok.*;

import javax.persistence.*;


@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "email", unique = true)
    private String email;

}
