package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class ItemRequestRepositoryTest {

    @Autowired
    private ItemRequestRepository itemRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGetItemRequestByIdOrderByCreated() {

        User user = User.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        ItemRequest itemRequest1 = ItemRequest.builder()
                .requestingUser(user)
                .description("description1")
                .created(now)
                .build();
        ItemRequest itemRequest2 = ItemRequest.builder()
                .requestingUser(user)
                .description("description2")
                .created(now.plusHours(1))
                .build();
        ItemRequest itemRequest3 = ItemRequest.builder()
                .requestingUser(user)
                .description("description3")
                .created(now.plusMinutes(30))
                .build();


        userRepository.save(user);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        itemRequestRepository.save(itemRequest3);

        assertNotNull(user.getId());
        assertNotNull(itemRequest1.getId());
        assertNotNull(itemRequest2.getId());
        assertNotNull(itemRequest3.getId());

        List<ItemRequest> itemRequests = itemRequestRepository.getItemRequestByRequestingUserIdOrderByCreated(user.getId());
        assertNotNull(itemRequests);
        assertEquals(3, itemRequests.size());
        assertEquals(itemRequest1, itemRequests.get(0));
        assertEquals(itemRequest3, itemRequests.get(1));
        assertEquals(itemRequest2, itemRequests.get(2));

    }

    @Test
    void testFindAllWithOutRequestingUser() {

        User user1 = User.builder()
                .name("user1")
                .email("user1@mail.com")
                .build();

        User user2 = User.builder()
                .name("user2")
                .email("user2@mail.com")
                .build();

        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());

        ItemRequest itemRequest1 = ItemRequest.builder()
                .requestingUser(user1)
                .description("description1")
                .created(now)
                .build();
        ItemRequest itemRequest2 = ItemRequest.builder()
                .requestingUser(user1)
                .description("description2")
                .created(now.plusHours(1))
                .build();
        ItemRequest itemRequest3 = ItemRequest.builder()
                .requestingUser(user2)
                .description("description3")
                .created(now.plusMinutes(30))
                .build();

        ItemRequest itemRequest4 = ItemRequest.builder()
                .requestingUser(user2)
                .description("description4")
                .created(now.plusMinutes(30))
                .build();


        userRepository.save(user1);
        userRepository.save(user2);
        itemRequestRepository.save(itemRequest1);
        itemRequestRepository.save(itemRequest2);
        itemRequestRepository.save(itemRequest3);
        itemRequestRepository.save(itemRequest4);

        assertNotNull(user1.getId());
        assertNotNull(user2.getId());
        assertNotNull(itemRequest1.getId());
        assertNotNull(itemRequest2.getId());
        assertNotNull(itemRequest3.getId());
        assertNotNull(itemRequest4.getId());

        List<ItemRequest> itemRequests = itemRequestRepository.findAllWithOutRequestingUser(user1.getId(), 0, 10);
        assertNotNull(itemRequests);
        assertEquals(2, itemRequests.size());
        assertEquals(itemRequest3, itemRequests.get(0));
        assertEquals(itemRequest4, itemRequests.get(1));

    }


}

