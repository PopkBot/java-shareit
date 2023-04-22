package ru.practicum.shareit.user.model;


import lombok.*;


@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
public class User {

    @EqualsAndHashCode.Exclude
    private Long id;
    @EqualsAndHashCode.Exclude
    private String name;
    private String email;
}
