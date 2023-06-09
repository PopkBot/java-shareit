package ru.practicum.gateway.booking.client;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.booking.dto.BookingInputDto;
import ru.practicum.gateway.booking.dto.BookingRequestParamsDto;
import ru.practicum.gateway.client.BaseClient;

import java.util.Map;

@Service
@Slf4j
public class BookingClient extends BaseClient {

    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addBooking(BookingInputDto bookingInputDto, Long bookerId) {
        log.info("Booking {} is requested", bookingInputDto);
        return post("", bookerId, bookingInputDto);
    }

    public ResponseEntity<Object> setApproveStatus(Long ownerId, Long bookingId, Boolean approved) {
        log.info("Booking {} set approved to {} from user {}", bookingId, approved, ownerId);
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId + "?approved=" + approved, ownerId.longValue());
    }

    public ResponseEntity<Object> getBookingsOfOwner(BookingRequestParamsDto dto) {
        log.info("All bookings of owner {}, status {}", dto.getOwnerId(), dto.getStatusString());
        Map<String, Object> parameters = Map.of(
                "state", dto.getStatusString(),
                "from", dto.getFrom(),
                "size", dto.getSize()
        );
        return get("/owner?state={state}&from={from}&size={size}", dto.getOwnerId(), parameters);
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        log.info("Booking of user {} is requested", userId);
        return get("/" + bookingId, userId);
    }

    @GetMapping("/bookings")
    public ResponseEntity<Object> getBookingsOfUser(BookingRequestParamsDto dto) {
        log.info("All bookings of user {}, status {}", dto.getOwnerId(), dto.getStatusString());
        Map<String, Object> parameters = Map.of(
                "state", dto.getStatusString(),
                "from", dto.getFrom(),
                "size", dto.getSize()
        );
        return get("?state={state}&from={from}&size={size}", dto.getOwnerId(), parameters);
    }
}
