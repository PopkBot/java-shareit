package ru.practicum.gateway.item.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.client.BaseClient;
import ru.practicum.gateway.item.dto.ItemClientDto;

@Service
@Slf4j
public class CommentClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public CommentClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addComment(ItemClientDto dto) {
        dto.getCommentInputDto().setAuthorId(dto.getUserId());
        dto.getCommentInputDto().setItemId(dto.getItemId());
        log.info("Comment {} adding is requested", dto.getCommentInputDto());
        return post("/" + dto.getItemId() + "/comment", dto.getUserId(), dto.getCommentInputDto());
    }

}
