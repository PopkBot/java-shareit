package ru.practicum.shareit.server.booking.model;

import lombok.*;
import ru.practicum.shareit.server.booking.Status;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.user.model.User;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    private ZonedDateTime start;
    @Column(name = "end_date", nullable = false)
    private ZonedDateTime end;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne
    @JoinColumn(name = "booker_id")
    private User booker;
    @OneToOne
    @JoinColumn(name = "item_id")
    private Item item;


}
