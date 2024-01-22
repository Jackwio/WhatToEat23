package com.my.Repository;

import com.my.pojo.Cart;
import com.my.pojo.CartItem;
import jakarta.persistence.QueryHint;
import jakarta.transaction.TransactionScoped;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface CartItemRepository extends CrudRepository<CartItem,Integer> {

    List<CartItem> findALLByCart_CartId(Integer cartId);
    CartItem findByFood_FoodIdAndCart_CartId(Integer foodId,Integer cartId);

    @Transactional
    @Modifying
    void deleteAllByCart_CartId(Integer CartId);

//    @Modifying
//    @Transactional
//    @Query("UPDATE CartItem c SET c.foodNum = ?1 WHERE c.cart.cartId = ?2 AND c.food.foodId = ?3")
//    void updateFoodNumByCart_CartIdAndFood_FoodId(Integer foodNum,Integer cartId ,Integer foodId);

}
