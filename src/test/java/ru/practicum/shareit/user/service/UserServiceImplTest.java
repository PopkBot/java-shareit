package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exceptions.ObjectAlreadyExists;
import ru.practicum.shareit.exceptions.ObjectNotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

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
        User userBlankName = User.builder()
                .name(" ")
                .email("user@user.com")
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.createUser(userBlankName));
        assertEquals("name cannot be blank", exception.getMessage());

        User userNullName = User.builder()
                .email("user@user.com")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.createUser(userNullName));
        assertEquals("name cannot be blank", exception.getMessage());

        User userNullEmail = User.builder()
                .name("Ams")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.createUser(userNullEmail));
        assertEquals("email cannot be blank", exception.getMessage());

        User userWrongEmailFormat = User.builder()
                .name("Ams")
                .email("incorrectEmail.m")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.createUser(userWrongEmailFormat));
        assertEquals("wrong email format", exception.getMessage());
    }

    @Test
    void createUserSuccess() {
        User user = User.builder()
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
        User user1 = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();

        User user2 = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();


        userService.createUser(user1);
        ObjectAlreadyExists exception = assertThrows(ObjectAlreadyExists.class, () -> userService.createUser(user2));
        assertEquals("unable to create user: user already exists", exception.getMessage());
    }


    @Test
    void updateUserValidationException() {


        User user1 = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user1).getId();
        User user2 = User.builder()
                .name("Amd")
                .email("incorrectEmailFormat")
                .build();
        ValidationException exception = assertThrows(ValidationException.class, () -> userService.updateUser(user2, userId));
        assertEquals("wrong email format", exception.getMessage());

        User user3 = User.builder()
                .name("Amd")
                .email("")
                .build();
        exception = assertThrows(ValidationException.class, () -> userService.updateUser(user3, userId));
        assertEquals("wrong email format", exception.getMessage());

    }

    @Test
    void updateNonExistingUser() {

        User user = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        ObjectNotFoundException exception = assertThrows(ObjectNotFoundException.class, () -> userService.updateUser(user, -1L));
        assertEquals("unable to update user: user not found", exception.getMessage());
    }

    @Test
    void updateOnAlreadyExistingUser() {

        User user1 = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        userService.createUser(user1);

        User user2 = User.builder()
                .name("Amd2")
                .email("user2@user.com")
                .build();
        Long userId = userService.createUser(user2).getId();

        User user2Up = User.builder()
                .name("Amd2U")
                .email("user@user.com")
                .build();
        ObjectAlreadyExists exception = assertThrows(ObjectAlreadyExists.class, () -> userService.updateUser(user2Up, userId));
        assertEquals("unable to update user: same user already exists", exception.getMessage());
    }

    @Test
    void updateUserSuccessful() {


        User user1 = User.builder()
                .name("Amd")
                .email("user@user.com")
                .build();
        Long userId = userService.createUser(user1).getId();
        User user1UpName = User.builder()
                .name("AmdUp")
                .build();
        userService.updateUser(user1UpName, userId);

        UserDto updatedUserDto = UserDto.builder()
                .id(userId)
                .name("AmdUp")
                .email("user@user.com")
                .build();
        assertEquals(updatedUserDto, userService.getUserById(userId));

        User user1UpEmail = User.builder()
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

        User user1 = User.builder()
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
            User user = User.builder()
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