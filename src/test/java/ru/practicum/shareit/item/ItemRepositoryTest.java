package ru.practicum.shareit.item;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.projection.ItemIdProjection;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testFindByUserIdWithPagination(){

        User user = User.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        Item item1 = Item.builder()
                .name("item1")
                .description("description")
                .available(true)
                .user(user)
                .build();
        Item item2 = Item.builder()
                .name("item2")
                .description("description")
                .available(true)
                .user(user)
                .build();
        Item item3 = Item.builder()
                .name("item3")
                .description("description")
                .available(true)
                .user(user)
                .build();
        Item item4 = Item.builder()
                .name("item4")
                .description("description")
                .available(true)
                .user(user)
                .build();

        userRepository.save(user);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);
        itemRepository.save(item4);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(item1.getId());
        Assertions.assertNotNull(item2.getId());
        Assertions.assertNotNull(item3.getId());
        Assertions.assertNotNull(item4.getId());

        int from = 1;
        int size = 2;

        List<Item> items = itemRepository.findAllByUserId(user.getId(),from,size);

        Assertions.assertNotNull(items);
        assertEquals(2,items.size());
        assertEquals(item2,items.get(0));
        assertEquals(item3,items.get(1));

    }

    @Test
    void testSearchByText(){

        User user = User.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        Item item1 = Item.builder()
                .name("TexT")
                .description("description")
                .available(true)
                .user(user)
                .build();

        Item item2 = Item.builder()
                .name("iTeM1")
                .description("descrTeXTiption")
                .available(true)
                .user(user)
                .build();

        Item item3 = Item.builder()
                .name("text")
                .description("text")
                .available(false)
                .user(user)
                .build();

        userRepository.save(user);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        Integer from = 0;
        Integer size = 10;
        String text = "%TEXT%";

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(item1.getId());
        Assertions.assertNotNull(item2.getId());
        Assertions.assertNotNull(item3.getId());

        List<Item> items = itemRepository.searchByQueryText(text,from,size);

        assertNotNull(items);
        assertEquals(2,items.size());
        assertEquals(item1,items.get(0));
        assertEquals(item2,items.get(1));

    }

    @Test
    void testFindByItemIdAndUserId(){

        User user = User.builder()
                .name("user")
                .email("user@mail.com")
                .build();

        Item item1 = Item.builder()
                .name("item1")
                .description("description")
                .available(true)
                .user(user)
                .build();


        userRepository.save(user);
        itemRepository.save(item1);

        Assertions.assertNotNull(user.getId());
        Assertions.assertNotNull(item1.getId());

        Optional<ItemIdProjection> itemIdProjection = itemRepository.findByIdAndUserId(item1.getId(),user.getId());
        assertEquals(item1.getId(),itemIdProjection.get().getId());

        itemIdProjection = itemRepository.findByIdAndUserId(item1.getId(),-1L);
        assertTrue(itemIdProjection.isEmpty());



    }

}
