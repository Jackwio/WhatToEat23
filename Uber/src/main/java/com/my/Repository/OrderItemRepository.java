package com.my.Repository;

import com.my.pojo.CartItem;
import com.my.pojo.OrderItem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends CrudRepository<OrderItem,Integer> {
    List<OrderItem> findAllByOrderId_OrderId(Long orderId);
}
