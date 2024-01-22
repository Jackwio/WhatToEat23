package com.my.Repository;

import com.my.pojo.Order;
import com.my.pojo.Ratings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingsRepository extends CrudRepository<Ratings, Order> {
    @Query("SELECT r.orderId.orderId FROM Ratings r WHERE r.member.memEmail = :memEmail")
    List<Long> findOrderIdByMember_MemEmail(@Param("memEmail") String memEmail);

    List<Ratings> findAllByOrderId_Restaurant_RestIdAndRatingsContentIsNotNull(Integer restId);

    List<Ratings> findAllByOrderId_Restaurant_RestId(Integer restId);
}
