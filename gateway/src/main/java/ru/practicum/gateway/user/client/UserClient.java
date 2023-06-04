package ru.practicum.gateway.user.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;
import ru.practicum.gateway.user.dto.UserInputDto;

@Slf4j
@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getUserById(Long userId) {
        log.info("user {} is requested", userId);
        return get("/" + userId);
    }

    public ResponseEntity<Object> createUser(UserInputDto user) {
        log.info("adding new user {} requested", user);
        return post("", user);
    }

    public ResponseEntity<Object> updateUser(UserInputDto user, Long userId) {
        log.info("updating of user {} requested", userId);
        return patch("/" + userId, userId, user);
    }

    public ResponseEntity<Object> deleteUserByID(Long userId) {
        log.info("deleting of user {} requested", userId);
        return delete("/" + userId);
    }

    public ResponseEntity<Object> getAllUsers() {
        log.info("all users are requested");
        return get("");
    }


}
