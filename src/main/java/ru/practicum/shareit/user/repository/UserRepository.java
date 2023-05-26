package ru.practicum.shareit.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.projection.UserIdProjection;


public interface UserRepository extends JpaRepository<User, Long> {


    UserIdProjection findByEmailIgnoreCaseAndIdNot(String email, Long userId);


}
