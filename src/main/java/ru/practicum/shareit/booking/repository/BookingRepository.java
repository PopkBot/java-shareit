package ru.practicum.shareit.booking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;



public interface BookingRepository extends JpaRepository<Booking, Long> {

    String TYPE_CASE = " case :user_type " +
            "when 'owner' then owner_id " +
            "when 'booker' then booker_id end";

    @Query(nativeQuery = true,
            value = "select * from bookings where id = ?1")
    Booking getBookingById(Long id);

    @Query(nativeQuery = true,
            value = "select count(*) from bookings where not (start_date >= ?2 or ?1 >= end_date) and owner_id = ?3 " +
                    "and status = 'APPROVED'")
    Long countDateOverlaps(String start, String end, Long ownerId);

    @Query(nativeQuery = true,
            value = "select * from bookings where " + TYPE_CASE + " = :user_id order by start_date desc")
    List<Booking> getAllBookingsOfOwner(@Param(value = "user_id") Long userId,
                                        @Param(value = "user_type") String userType);

    @Query(nativeQuery = true,
            value = "select * from bookings where " + TYPE_CASE +
                    " = :user_id and status = :status order by start_date desc")
    List<Booking> getBookingsOfOwnerByApproval(@Param(value = "user_id") Long userId,
                                               @Param(value = "status") String status,
                                               @Param(value = "user_type") String userType);

    @Query(nativeQuery = true,
            value = "select * from bookings where " + TYPE_CASE + " = :user_id " +
                    "and end_date < :date order by start_date desc")
    List<Booking> getPastBookingsOfOwner(@Param(value = "user_id") Long userId,
                                         @Param(value = "date") String now,
                                         @Param(value = "user_type") String userType);

    @Query(nativeQuery = true,
            value = "select * from bookings where " + TYPE_CASE + " = :user_id " +
                    "and start_date > :date order by end_date desc")
    List<Booking> getFutureBookingsOfOwner(@Param(value = "user_id") Long userId,
                                           @Param(value = "date") String now,
                                           @Param(value = "user_type") String userType);

    @Query(nativeQuery = true,
            value = "select * from bookings where " + TYPE_CASE + " = :user_id " +
                    "and start_date <= :date and end_date >= :date order by start_date desc")
    List<Booking> getCurrentBookingsOfOwner(@Param(value = "user_id") Long userId,
                                            @Param(value = "date") String now,
                                            @Param(value = "user_type") String userType);

    @Query(nativeQuery = true,
            value = "select top 1 * from bookings where item_id = :item_id " +
                    "and start_date >= :date and status = 'APPROVED' order by end_date ")
    Booking getNextBooking(@Param(value = "date") String now,
                           @Param(value = "item_id") Long itemId);

    @Query(nativeQuery = true,
            value = "select top 1 * from bookings where item_id = :item_id " +
                    "and start_date <= :date and status = 'APPROVED' order by end_date desc")
    Booking getLastBooking(@Param(value = "date") String now,
                           @Param(value = "item_id") Long itemId);


    @Query(nativeQuery = true,
            value = "select count(*) from bookings where booker_id = :booker_id " +
                    "and item_id = :item_id and UPPER(status) = UPPER(:status) " +
                    "and start_date < :date_string")
    Long countByBookerIdAndItemIdAndStatus(@Param(value = "booker_id") Long bookerId,
                                           @Param(value = "item_id") Long itemId,
                                           @Param(value = "status") String status,
                                           @Param(value = "date_string") String now);

}
