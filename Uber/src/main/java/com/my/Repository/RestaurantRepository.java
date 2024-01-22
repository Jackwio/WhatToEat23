package com.my.Repository;

import com.my.pojo.Restaurant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RestaurantRepository extends CrudRepository<Restaurant, Integer> {

    List<Restaurant> findAll();

    Restaurant findByRestId(Integer restId);

    Page<Restaurant> findAllByRestIdIn(Collection<Integer> restId, Pageable pageable);
    Page<Restaurant> findAllByRestIdInOrderByRestRatingsDesc(Collection<Integer> restId, Pageable pageable);

    Restaurant findByRestIdAndIsDeleted(Integer restId, Integer isDeleted);
    Restaurant findByOwnerEmail(String ownerEmail);
}
