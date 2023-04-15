package ru.practicum.shareit.user.model;


import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@Builder
@ToString
public class User {

    @EqualsAndHashCode.Exclude
    @NotNull
    private Long userId;
    @NotBlank(message = "name cannot be blank")
    @EqualsAndHashCode.Exclude
    private String name;
    @NotBlank(message = "email cannot be blank")
    @Email(message = "wrong email format")
    private String email;
}
