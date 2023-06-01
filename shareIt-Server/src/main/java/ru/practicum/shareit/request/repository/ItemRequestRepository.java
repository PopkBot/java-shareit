package ru.practicum.shareit.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM item_requests WHERE requesting_user_id = ?1 " +
                    "ORDER BY created")
    List<ItemRequest> getRequestsOfUser(Long requestingUserId);

    List<ItemRequest> getItemRequestByRequestingUserIdOrderByCreated(Long requestingUserId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM item_requests WHERE requesting_user_id <> ?1 limit ?3 offset ?2")
    List<ItemRequest> findAllWithOutRequestingUser(Long userId, Integer from, Integer size);

}
