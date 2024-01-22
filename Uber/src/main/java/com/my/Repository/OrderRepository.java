package com.my.Repository;

import com.my.Enum.OrderState;
import com.my.Enum.RestOrderState;
import com.my.pojo.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order,Long> {
    Order findByOrderDateTimeOrderByOrderDateTimeDesc(LocalDateTime localDateTime);
    List<Order> findAllByMember_MemEmail(String memEmail);
    List<Order> findAllByRestaurant_RestId(Integer restId);
    List<Order> findAllByRestaurant_RestIdAndRestAccepted(Integer restId, RestOrderState restOrderState);
    List<Order> findAllByMember_MemEmailAndRestAccepted(String memEmail,RestOrderState restOrderState);

    List<Order> findAllByOrderStateAndRestaurant_RestId(OrderState orderState,Integer restId);
}
