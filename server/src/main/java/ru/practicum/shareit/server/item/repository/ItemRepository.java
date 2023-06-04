package ru.practicum.shareit.server.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.server.item.model.Item;
import ru.practicum.shareit.server.item.projection.ItemIdProjection;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query(value = "select * from items where user_id = ?1 order by id", nativeQuery = true)
    Page<Item> findAllByUserId(Long userId, Pageable pageable);

    Optional<ItemIdProjection> findByIdAndUserId(Long itemId, Long userId);

    @Query(value = "select * from items as it where it.available = true and " +
            "(UPPER(it.description) like ?1 or UPPER(it.name) like ?1 ) limit ?3 offset ?2", nativeQuery = true)
    List<Item> searchByQueryText(String queryText, Integer from, Integer size);

}
