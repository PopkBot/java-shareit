package ru.practicum.shareit.booking.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.Instant;

@Getter
@Setter
@Entity
@EqualsAndHashCode
@ToString
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date",nullable = false)
    private Instant start;
    @Column(name = "end_date",nullable = false)
    private Instant end;
    @Column(name = "status",nullable = false)
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
