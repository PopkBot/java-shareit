package ru.practicum.shareit.user.repository;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserRepository {

    List<User> getAllUsers();

    User getUserById(Long userId);

    User addUser(User user);

    User updateUser(User user, Long userId);

    User deleteUser(Long userId);

    boolean isContainUser(Long id);

    boolean isContainUser(User user);

    boolean isContainUser(User user, Long userId);

}
