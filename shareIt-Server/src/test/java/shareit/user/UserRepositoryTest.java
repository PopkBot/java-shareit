package shareit.user;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.projection.UserIdProjection;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void testSaveUser() {
        User user = User.builder()
                .name("user")
                .email("email@email.com")
                .build();
        Assertions.assertNull(user.getId());
        userRepository.save(user);
        Assertions.assertNotNull(user.getId());
    }

    @Test
    void testFindUserByEmail() {

        User user1 = User.builder()
                .name("user1")
                .email("email1@email.com")
                .build();
        User user2 = User.builder()
                .name("user2")
                .email("email2@email.com")
                .build();
        User user3 = User.builder()
                .name("user3")
                .email("email3@email.com")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Assertions.assertNotNull(user1.getId());
        Assertions.assertNotNull(user2.getId());
        Assertions.assertNotNull(user3.getId());

        UserIdProjection foundUserId = userRepository.findByEmailIgnoreCaseAndIdNot("email1@email.com", user3.getId());
        assertEquals(user1.getId(), foundUserId.getId());
        foundUserId = userRepository.findByEmailIgnoreCaseAndIdNot("email2@email.com", user3.getId());
        assertEquals(user2.getId(), foundUserId.getId());
        foundUserId = userRepository.findByEmailIgnoreCaseAndIdNot("email3@email.com", user3.getId());
        Assertions.assertNull(foundUserId);

    }

    @Test
    void testFindById() {

        User user1 = User.builder()
                .name("user1")
                .email("email1@email.com")
                .build();
        userRepository.save(user1);
        Assertions.assertNotNull(user1.getId());
        Optional<User> foundUser = userRepository.findById(user1.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user1, foundUser.get());

        Optional<User> notFoundUser = userRepository.findById(-1L);
        assertTrue(notFoundUser.isEmpty());

    }

    @Test
    void testFindAll() {

        User user1 = User.builder()
                .name("user1")
                .email("email1@email.com")
                .build();
        User user2 = User.builder()
                .name("user2")
                .email("email2@email.com")
                .build();
        User user3 = User.builder()
                .name("user3")
                .email("email3@email.com")
                .build();
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);

        Assertions.assertNotNull(user1.getId());
        Assertions.assertNotNull(user2.getId());
        Assertions.assertNotNull(user3.getId());

        List<User> users = userRepository.findAll();
        assertNotNull(users);
        assertEquals(3, users.size());
        assertEquals(user1, users.get(0));
        assertEquals(user2, users.get(1));
        assertEquals(user3, users.get(2));

    }


}

