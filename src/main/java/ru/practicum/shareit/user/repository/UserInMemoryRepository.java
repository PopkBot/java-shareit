package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;

@Component
public class UserInMemoryRepository implements UserRepository{

    private final HashMap<Long,User> users = new HashMap<>();
    private Long userIdCount=1L;

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public User addUser(User user) {
        users.put(userIdCount,user);
        return getUserById(++userIdCount);
    }

    @Override
    public User updateUser(User user, Long userId) {
        users.put(userId,user);
        return getUserById(userId);
    }

    @Override
    public User deleteUser(Long userId) {
        User removedUser = getUserById(userId);
        users.remove(userId);
        return removedUser;
    }

    @Override
    public boolean isContainUser(Long id) {
        return users.containsKey(id);
    }

    @Override
    public boolean isContainUser(User user) {
        return users.containsValue(user);
    }
}
