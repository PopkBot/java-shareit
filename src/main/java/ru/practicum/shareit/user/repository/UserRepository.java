package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.projection.UserEmailProjection;
import ru.practicum.shareit.user.model.User;


public interface UserRepository extends JpaRepository<User,Long> {

    UserEmailProjection findByEmailIgnoreCase(String email);

    UserEmailProjection findByEmailIgnoreCaseAndIdNot(String email,Long userId);

    /*List<User> getAllUsers();

    User getUserById(Long userId);

    User addUser(User user);

    User updateUser(User user, Long userId);

    User deleteUser(Long userId);

    boolean isContainUser(Long id);

    boolean isContainUser(User user);

    boolean isContainUser(User user, Long userId);*/

}
