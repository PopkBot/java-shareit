package ru.practicum.shareit.item.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.ItemClientDto;
import ru.practicum.shareit.item.dto.ItemInputDto;

import java.util.Map;

@Service
@Slf4j
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        log.info("item {} requested", itemId);
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> addItem(ItemInputDto itemInputDto, Long userId) {
        log.info("adding new item {} requested", itemInputDto);
        return post("", userId, itemInputDto);
    }

    public ResponseEntity<Object> updateItem(ItemClientDto dto) {
        log.info("updating item requested");
        dto.getItemInputDto().setId(dto.getItemId());
        return put("/" + dto.getItemId(), dto.getUserId(), dto.getItemInputDto());
    }

    public ResponseEntity<Object> searchItem(ItemClientDto dto) {
        log.info("search for {}", dto.getSearchText());
        Map<String, Object> parameters = Map.of(
                "text", dto.getSearchText(),
                "from", dto.getFrom(),
                "size", dto.getSize()
        );
        return get("/search", dto.getUserId(), parameters);
    }

    public ResponseEntity<Object> deleteItemById(Long itemId) {
        log.info("deleting item {} requested", itemId);
        return delete("/" + itemId);
    }

    public ResponseEntity<Object> getAllItemsOfUser(ItemClientDto dto) {
        log.info("all items of user {} are requested", dto.getUserId());
        Map<String, Object> parameters = Map.of(
                "from", dto.getFrom(),
                "size", dto.getSize()
        );
        return get("", dto.getUserId(), parameters);
    }
}
