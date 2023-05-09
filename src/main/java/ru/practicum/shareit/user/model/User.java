package ru.practicum.shareit.user.model;


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
    @EqualsAndHashCode.Exclude
    private Long id;
    @Column(name = "name",nullable = false)
    @EqualsAndHashCode.Exclude
    private String name;
    @Column(name = "email",unique = true)
    private String email;

}
