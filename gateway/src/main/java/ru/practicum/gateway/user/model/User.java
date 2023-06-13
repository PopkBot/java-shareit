package ru.practicum.gateway.user.model;


import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String email;

}
