package com.my.Repository;

import com.my.pojo.Food;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends BaseRepository<Food,Integer> {

    List<Food> findAllByRestaurant_RestId(Integer restId);
    List<Food> findAllByRestaurant_RestIdAndFoodNameContaining(Integer restId,String foodName);

    @Procedure
    List<String> findDistinctFoodTypeByRestaurant_RestId(@Param("restId") Integer restId);

    Food findByFoodId(Integer foodId);

    Food findByFoodNameAndRestaurant_RestId(String foodName,Integer restId);
}
