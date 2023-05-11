package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.projection.UserIdProjection;
import ru.practicum.shareit.user.model.User;


public interface UserRepository extends JpaRepository<User,Long> {

    UserIdProjection findByEmailIgnoreCase(String email);

    UserIdProjection findByEmailIgnoreCaseAndIdNot(String email, Long userId);


}
