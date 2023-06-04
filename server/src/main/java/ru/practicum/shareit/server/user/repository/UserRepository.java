package ru.practicum.shareit.server.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.server.user.model.User;
import ru.practicum.shareit.server.user.projection.UserIdProjection;


public interface UserRepository extends JpaRepository<User, Long> {


    UserIdProjection findByEmailIgnoreCaseAndIdNot(String email, Long userId);


}
