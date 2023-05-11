package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.projection.ItemIdProjection;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item,Long> {

    List<Item> findAllByUserId(Long userId);

    Optional<ItemIdProjection> findByIdAndUserId(Long itemId, Long userId);

    @Query(value = "select * from items as it where it.available = true and "+
                "(UPPER(it.description) like ?1 or UPPER(it.name) like ?1 )",nativeQuery = true)
    List<Item> searchByQueryText( String queryText);

}
