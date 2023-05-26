package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestInputDto;
import ru.practicum.shareit.request.sevice.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceImpTest {

    private final ItemRequestService itemRequestService;
    private final UserService userService;

    @Test
    void testAddRequest() {


        ItemRequestInputDto inputDto = ItemRequestInputDto.builder()
                .description("description")
                .build();

        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.addRequest(inputDto, -1L));
        assertEquals("User not found", oe.getMessage());

        User user = User.builder()
                .name("name")
                .email("email@mail.com")
                .build();
        user.setId(userService.createUser(user).getId());

        ItemRequestDto itemRequestDto = itemRequestService.addRequest(inputDto, user.getId());
        assertEquals(inputDto.getDescription(), itemRequestDto.getDescription());

    }


    @Test
    void testGetItemRequest() {

        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getItemRequestsOfUser(-1L));
        assertEquals("user not found", oe.getMessage());

        ItemRequestInputDto inputDto = ItemRequestInputDto.builder()
                .description("description")
                .build();

        User user = User.builder()
                .name("name")
                .email("email@mail.com")
                .build();
        user.setId(userService.createUser(user).getId());
        itemRequestService.addRequest(inputDto, user.getId());
        itemRequestService.addRequest(inputDto, user.getId());
        List<ItemRequestDto> itemRequestDtos = itemRequestService.getItemRequestsOfUser(user.getId());
        assertEquals(2, itemRequestDtos.size());
        assertEquals(inputDto.getDescription(), itemRequestDtos.get(0).getDescription());
        assertEquals(inputDto.getDescription(), itemRequestDtos.get(1).getDescription());

    }


    @Test
    void testGetAllRequestsPaged() {

        ItemRequestInputDto inputDto1 = ItemRequestInputDto.builder()
                .description("description1")
                .build();
        ItemRequestInputDto inputDto2 = ItemRequestInputDto.builder()
                .description("description2")
                .build();
        ItemRequestInputDto inputDto3 = ItemRequestInputDto.builder()
                .description("description3")
                .build();
        User user1 = User.builder()
                .name("name1")
                .email("email1@mail.com")
                .build();
        user1.setId(userService.createUser(user1).getId());
        User user2 = User.builder()
                .name("name2")
                .email("email2@mail.com")
                .build();
        user2.setId(userService.createUser(user2).getId());
        itemRequestService.addRequest(inputDto1, user1.getId());
        itemRequestService.addRequest(inputDto2, user1.getId());
        itemRequestService.addRequest(inputDto3, user2.getId());

        List<ItemRequestDto> itemRequestDtos = itemRequestService.getAllRequestsPaged(-1L, 0, 10);
        assertEquals(3, itemRequestDtos.size());

        List<ItemRequestDto> itemRequestDtos2 = itemRequestService.getAllRequestsPaged(user2.getId(), 0, 10);
        assertEquals(2, itemRequestDtos2.size());
        assertEquals(inputDto1.getDescription(), itemRequestDtos2.get(0).getDescription());
        assertEquals(inputDto2.getDescription(), itemRequestDtos2.get(1).getDescription());

    }

    @Test
    void testGetItemRequestById() {

        ObjectNotFoundException oe = assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getItemRequestById(-1L, -1L));
        assertEquals("user not found", oe.getMessage());
        User user = User.builder()
                .name("name")
                .email("email@mail.com")
                .build();
        user.setId(userService.createUser(user).getId());
        oe = assertThrows(ObjectNotFoundException.class,
                () -> itemRequestService.getItemRequestById(user.getId(), -1L));
        assertEquals("item request not found", oe.getMessage());

        ItemRequestInputDto inputDto = ItemRequestInputDto.builder()
                .description("description")
                .build();
        ItemRequestDto createdItemRequestDto = itemRequestService.addRequest(inputDto, user.getId());
        ItemRequestDto itemRequestDto = itemRequestService
                .getItemRequestById(user.getId(), createdItemRequestDto.getId());
        assertEquals(createdItemRequestDto.getDescription(), itemRequestDto.getDescription());

    }

}
