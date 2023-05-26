package ru.practicum.shareit.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.sevice.ItemRequestService;
import ru.practicum.shareit.user.controller.UserController;
import ru.practicum.shareit.user.service.UserService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    private MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUpItemRequestDto() {
        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .build();
    }

    @Test
    void testAddRequest() throws Exception {

        ItemRequestInputDto inputDto = ItemRequestInputDto.builder()
                .build();

        when(itemRequestService.addRequest(any(), any()))
                .thenReturn(itemRequestDto);

        mvc.perform(post("/requests")
                .header("X-Sharer-User-Id", 1L)
                .content(mapper.writeValueAsString(null))
                .characterEncoding(StandardCharsets.UTF_8)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        inputDto.setDescription(" ");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        inputDto.setDescription("description");

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(inputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class));

    }

    @Test
    void testGetRequestsOfUser() throws Exception {

        when(itemRequestService.getItemRequestsOfUser(any()))
                .thenReturn(List.of(itemRequestDto,itemRequestDto));
        mvc.perform(get("/requests").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].description", is(itemRequestDto.getDescription()), String.class));

    }

    @Test
    void testGetRequestById() throws Exception {

        when(itemRequestService.getItemRequestById(any(),any()))
                .thenReturn(itemRequestDto);
        mvc.perform(get("/requests/1").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription()), String.class));

    }

    @Test
    void testGetAllRequests() throws Exception {

        when(itemRequestService.getAllRequestsPaged(any(),any(),any()))
                .thenReturn(List.of(itemRequestDto,itemRequestDto));
        mvc.perform(get("/requests/all").header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].description", is(itemRequestDto.getDescription()), String.class))
                .andExpect(jsonPath("$.[1].description", is(itemRequestDto.getDescription()), String.class));

    }

}
