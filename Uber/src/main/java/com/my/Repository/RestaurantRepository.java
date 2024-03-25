package com.my.Repository;

import com.my.pojo.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RestaurantRepository extends BaseRepository<Restaurant, Integer> {

    List<Restaurant> findAll();

    @EntityGraph(value = "Restaurant.foods", type = EntityGraph.EntityGraphType.FETCH)
    Restaurant findByRestId(Integer restId);

    Page<Restaurant> findAllByRestIdIn(Collection<Integer> restId, Pageable pageable);

    Page<Restaurant> findAllByRestIdInOrderByRestRatingsDesc(Collection<Integer> restId, Pageable pageable);

    Restaurant findByRestIdAndIsDeleted(Integer restId, Integer isDeleted);

    @EntityGraph(value = "Restaurant.foods", type = EntityGraph.EntityGraphType.FETCH)
    Restaurant findByOwnerEmail(String ownerEmail);
}
