package shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingInputDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;
    @MockBean
    private BookingService bookingService;
    @Autowired
    private MockMvc mvc;
    private BookingDto bookingDto;

    @BeforeEach
    void setUpBookingDto() {
        bookingDto = BookingDto.builder()
                .id(1L)
                .status(Status.APPROVED)
                .booker(BookingDto.builder().build().getBooker())
                .start(Date.from(Instant.now()))
                .end(Date.from(Instant.now()))
                .item(ItemDto.builder().build())
                .build();
    }


    @Test
    void testAddBooking() throws Exception {

        when(bookingService.addBooking(any(), any()))
                .thenReturn(bookingDto);

        BookingInputDto bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .end(LocalDateTime.now().minusHours(1))
                .build();

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .build();
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .start(LocalDateTime.now().plusHours(1))
                .build();
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        LocalDateTime now = LocalDateTime.now();
        bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .start(now)
                .end(now)
                .build();
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .start(now.minusHours(1))
                .end(now.minusHours(10))
                .build();
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        bookingInputDto = BookingInputDto.builder()
                .itemId(1L)
                .start(now.plusHours(1))
                .end(now.plusHours(10))
                .build();
        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(bookingInputDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

    }

    @Test
    void testSetApprovedStatus() throws Exception {

        when(bookingService.setApprovedStatus(any(), any(), anyBoolean()))
                .thenReturn(bookingDto);
        mvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

    }

    @Test
    void testBookingsOfOwner() throws Exception {

        when(bookingService.getBookingsOfUser(any()))
                .thenReturn(List.of(bookingDto, bookingDto));
        mvc.perform(get("/bookings/owner?state=All&from=0&size=10")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[1].id", is(bookingDto.getId()), Long.class));
    }

    @Test
    void testBookingsOfBooker() throws Exception {

        when(bookingService.getBookingsOfUser(any()))
                .thenReturn(List.of(bookingDto, bookingDto));
        mvc.perform(get("/bookings?state=All&from=0&size=10")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", is(bookingDto.getId()), Long.class))
                .andExpect(jsonPath("$.[1].id", is(bookingDto.getId()), Long.class));
    }

    @Test
    void testGetBookingById() throws Exception {

        when(bookingService.getBookingById(any(), any()))
                .thenReturn(bookingDto);
        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class));

    }

}
