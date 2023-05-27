package ru.practicum.shareit.item;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Optional;

@DataJpaTest
public class CommentsRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    void testCommentSave(){

        User user = User.builder()
                .name("name")
                .email("email@email.com")
                .build();

        Item item = Item.builder()
                .user(user)
                .name("item")
                .description("description")
                .available(true)
                .build();

        userRepository.save(user);
        itemRepository.save(item);

        Comment comment = Comment.builder()
                .text("text")
                .created(1L)
                .author(user)
                .itemId(item.getId())
                .build();

        commentRepository.save(comment);
        assertNotNull(comment.getId());

        Optional<Comment> foundComment = commentRepository.findById(comment.getId());

        assertTrue(foundComment.isPresent());
        assertEquals(comment,foundComment.get());

    }

}
