package com.my.Repository;

import com.my.pojo.Food;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends CrudRepository<Food,Integer> {

    List<Food> findAllByRestaurant_RestId(Integer restId);

    @Query("SELECT DISTINCT f.foodType FROM Food f WHERE f.restaurant.restId = ?1")
    List<String> findDistinctFoodTypeByRestaurant_RestId(Integer restId);

    Food findByFoodId(Integer foodId);

    Food findByFoodNameAndRestaurant_RestId(String foodName,Integer restId);
}
