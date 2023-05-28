package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserInputDto;
import ru.practicum.shareit.user.service.UserService;

import javax.transaction.Transactional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
class UserServiceImplTest {

    private final UserService userService;


    @Test
    void createUserValidationException() {
        UserInputDto userBlankName = UserInputDto.builder()
                .name(" ")
                .email("user@user.com")
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.createUser(userBlankName));
        assertEquals("name cannot be blank", exception.getMessage());

        UserInputDto userNullName = UserInputDto.builder()
                .email("user@user.com")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.createUser(userNullName));
        assertEquals("name cannot be blank", exception.getMessage());

        UserInputDto userNullEmail = UserInputDto.builder()
                .name("Ams")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.createUser(userNullEmail));
        assertEquals("email cannot be blank", exception.getMessage());

        UserInputDto userWrongEmailFormat = UserInputDto.builder()
                .name("Ams")
                .email("incorrectEmail.m")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.createUser(userWrongEmailFormat));
        assertEquals("wrong email format", exception.getMessage());
    }

    @Test
    void createUserSuccess() {
        UserInputDto user = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();


        UserDto createdUserDto = userService.createUser(user);

        UserDto expectedUserDto = UserDto.builder()
                .id(createdUserDto.getId())
                .name("Amd")
                .email("user@user.com")
                .build();

        assertEquals(expectedUserDto, createdUserDto);

    }

    @Test
    void throwExceptionOnCreateOfSameUser() {
        UserInputDto user1 = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();

        UserInputDto user2 = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();


        userService.createUser(user1);
        ObjectAlreadyExists exception = assertThrows(ObjectAlreadyExists.class, () -> userService.createUser(user2));
        assertEquals("unable to create user: user already exists", exception.getMessage());
    }


    @Test
    void updateUserValidationException() {


        UserInputDto user1 = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user1).getId();
        UserInputDto user2 = UserInputDto.builder()
                .name("Amd")
                .email("incorrectEmailFormat")
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.updateUser(user2, userId));
        assertEquals("wrong email format", exception.getMessage());

        UserInputDto user3 = UserInputDto.builder()
                .name("Amd")
                .email("")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.updateUser(user3, userId));
        assertEquals("wrong email format", exception.getMessage());

    }

    @Test
    void updateNonExistingUser() {

        UserInputDto user = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> userService.updateUser(user, -1L));
        assertEquals("unable to update user: user not found", exception.getMessage());
    }

    @Test
    void updateOnAlreadyExistingUser() {

        UserInputDto user1 = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        userService.createUser(user1);

        UserInputDto user2 = UserInputDto.builder()
                .name("Amd2")
                .email("user2@user.com")
                .build();
        Long userId = userService.createUser(user2).getId();

        UserInputDto user2Up = UserInputDto.builder()
                .name("Amd2U")
                .email("user@user.com")
                .build();
        ObjectAlreadyExists exception = assertThrows(ObjectAlreadyExists.class, () -> userService.updateUser(user2Up, userId));
        assertEquals("unable to update user: same user already exists", exception.getMessage());
    }

    @Test
    void updateUserSuccessful() {


        UserInputDto user1 = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user1).getId();
        UserInputDto user1UpName = UserInputDto.builder()
                .name("AmdUp")
                .build();
        userService.updateUser(user1UpName, userId);

        UserDto updatedUserDto = UserDto.builder()
                .id(userId)
                .name("AmdUp")
                .email("user@user.com")
                .build();
        assertEquals(updatedUserDto, userService.getUserById(userId));

        UserInputDto user1UpEmail = UserInputDto.builder()
                .email("userUp@user.com")
                .build();
        userService.updateUser(user1UpEmail, userId);

        updatedUserDto = UserDto.builder()
                .id(userId)
                .name("AmdUp")
                .email("userUp@user.com")
                .build();
        assertEquals(updatedUserDto, userService.getUserById(userId));
    }

    @Test
    void throwExceptionOnDeletingNonExistingUser() {
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class,
                () -> userService.deleteUser(-1L));
        assertEquals("unable to delete user: user not exists", exception.getMessage());
    }

    @Test
    void deleteExistingUserSuccessful() {

        UserInputDto user1 = UserInputDto.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user1).getId();

        UserDto expectedUserDto = userService.getUserById(userId);

        UserDto deletedUserDto = userService.deleteUser(userId);
        assertEquals(expectedUserDto, deletedUserDto);
    }

    @Test
    void testGetAllUsers() {

        assertEquals(0, userService.getAllUsers().size());

        ArrayList<UserDto> userDtos = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            UserInputDto user = UserInputDto.builder()
                    .name("Amd" + i)
                    .email("user" + i + "@user.com")
                    .build();
            userDtos.add(userService.createUser(user));
        }
        ArrayList<UserDto> returnedUserDtos = (ArrayList<UserDto>) userService.getAllUsers();
        assertEquals(userDtos.size(), returnedUserDtos.size());
        assertEquals(userDtos, returnedUserDtos);
    }

}