package ru.practicum.gateway.request.client;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;
import ru.practicum.gateway.request.dto.ItemRequestInputDto;

import java.util.Map;

@Service
@Slf4j
public class ItemRequestClient extends BaseClient {

    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addRequest(Long userId, ItemRequestInputDto inputDto) {
        log.info("adding of item request {}", inputDto);
        return post("", userId, inputDto);
    }

    public ResponseEntity<Object> getRequestsOfUser(Long userId) {
        log.info("list of item requests of user {} is requested", userId);
        return get("", userId);
    }

    public ResponseEntity<Object> getAllRequestsPaged(Long userId, Integer from, Integer size) {
        log.info("list of all requests is requested from {} with page size {}", from, size);
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("?from={state}", userId, parameters);
    }

    public ResponseEntity<Object> getRequestById(Long userId, Long requestId) {
        log.info("item request {} is requested", requestId);
        return get("/" + requestId, userId);
    }
}
