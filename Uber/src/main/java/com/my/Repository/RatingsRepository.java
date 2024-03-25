package com.my.Repository;

import com.my.pojo.Order;
import com.my.pojo.Ratings;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface RatingsRepository extends BaseRepository<Ratings, Order> {
    @Procedure
    List<Long> findOrderIdByMember_MemEmail(@Param("memEmail") String memEmail);

    List<Ratings> findAllByOrderId_Restaurant_RestIdAndRatingsContentIsNotNull(Integer restId);

}
