package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserInMemoryRepository implements UserRepository {

    private final HashMap<Long, User> users = new HashMap<>();
    private Long userIdCount = 1L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public User addUser(User user) {
        user.setId(userIdCount);
        users.put(userIdCount, user);
        userIdCount++;
        return user;
    }

    @Override
    public User updateUser(User user, Long userId) {
        User userToUpdate = users.get(userId);
        if (user.getEmail() != null) {
            userToUpdate.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            userToUpdate.setName(user.getName());
        }

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
    public boolean isContainUser(User user, Long userId) {
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if (entry.getValue().equals(user) && !entry.getKey().equals(userId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isContainUser(User user) {
        return users.containsValue(user);
    }

}
