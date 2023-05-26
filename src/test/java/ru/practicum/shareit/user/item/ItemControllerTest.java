package ru.practicum.shareit.user.item;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentInputDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {


    @Autowired
    ObjectMapper mapper;
    @MockBean
    private ItemService itemService;
    @Autowired
    private MockMvc mvc;
    private ItemDto itemDto;

    @BeforeEach
    void setUpItemDto() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("item name")
                .description("description")
                .build();
    }


    @Test
    void throwExceptionOnAddingInvalidItem() throws Exception {

        Item itemBlankName = Item.builder()
                .name(" ")
                .description("description")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemBlankName))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Item itemNullName = Item.builder()
                .description("description")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemNullName))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Item itemBlankDescription = Item.builder()
                .name("Amd")
                .description(" ")
                .available(true)
                .build();

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemBlankDescription))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Item itemNullAvailable = Item.builder()
                .name("Amd")
                .description("description")
                .build();

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(itemNullAvailable))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void testAddingValidItem() throws Exception {

        Item item = Item.builder()
                .name("item name")
                .description("description")
                .available(true)
                .build();

        when(itemService.addItem(any(), any()))
                .thenReturn(itemDto);
        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));

    }

    @Test
    void testGetItemById() throws Exception {

        when(itemService.getItemById(any(), any()))
                .thenReturn(itemDto);


        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));

    }

    @Test
    void testUpdateItem() throws Exception {

        when(itemService.updateItem(any(), any()))
                .thenReturn(itemDto);

        Item item = Item.builder()
                .name("item name")
                .description("description")
                .available(true)
                .build();

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(item))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));

    }

    @Test
    void testSearchItem() throws Exception {

        when(itemService.searchItem(any(), any(), any()))
                .thenReturn(List.of(itemDto, itemDto));

        mvc.perform(get("/items/search?text=text"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription()), String.class));

    }

    @Test
    void testDeleteItem() throws Exception {

        when(itemService.deleteItem(any()))
                .thenReturn(itemDto);

        mvc.perform(delete("/items/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription()), String.class));

    }

    @Test
    void testGetAllItems() throws Exception {

        when(itemService.getAllItemsOfUser(any(), any(), any()))
                .thenReturn(List.of(itemDto, itemDto));

        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[0].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.[0].description", is(itemDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.[1].name", is(itemDto.getName()), String.class))
                .andExpect(jsonPath("$.[1].description", is(itemDto.getDescription()), String.class));

    }

    @Test
    void testAddComment() throws Exception {

        CommentDto commentDto = CommentDto.builder()
                .text("comment")
                .authorName("user")
                .id(1L)
                .created(LocalDateTime.now())
                .build();

        CommentInputDto requiredCommentInputDto = CommentInputDto.builder()
                .itemId(1L)
                .authorId(1L)
                .text("comment")
                .build();

        when(itemService.addComment(requiredCommentInputDto))
                .thenReturn(commentDto);

        CommentInputDto commentInputDto = CommentInputDto.builder()
                .text("comment")
                .build();

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(commentInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto.getText()), String.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName()), String.class))
                .andExpect(jsonPath("$.created",
                        is(commentDto.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))),
                        LocalDateTime.class));
    }

}
