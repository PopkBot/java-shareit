package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest,Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM item_requests WHERE requesting_user_id = ?1 "+
            "ORDER BY created")
    public List<ItemRequest> getRequestsOfUser(Long requestingUserId);

    @Query(nativeQuery = true,
            value = "SELECT * FROM item_requests WHERE requesting_user_id <> ?1")
    public Page<ItemRequest> findAllWithOutRequestingUser(Long userId, Pageable pageable);

}