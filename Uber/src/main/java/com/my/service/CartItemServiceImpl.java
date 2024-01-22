package com.my.service;

import com.my.Repository.CartItemRepository;
import com.my.Repository.FoodRepository;
import com.my.pojo.Cart;
import com.my.pojo.CartItem;
import com.my.pojo.Food;
import com.my.pojo.Member;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public void addCartItem(HttpSession session, Integer foodId) {
        changeCartItem(session, foodId, 1);
    }

    @Override
    public void editCartItem(HttpSession session, Integer foodId, Integer change) {
        changeCartItem(session, foodId, change);
    }

    @Override
    public void deleteCartItem(HttpSession session, Integer foodId) {
        Integer change = Integer.MAX_VALUE;
        changeCartItem(session, foodId, change);
    }

    @Transactional
    @Override
    public void changeCartItem(HttpSession session, Integer foodId, Integer change) {

        Member currentUser;
        List<CartItem> cartItems = new ArrayList<>();
        Food food = foodRepository.findByFoodId(foodId);

        Object currentUserObj = session.getAttribute("currentUser");

        if (currentUserObj == null) {
            Map<Integer, CartItem> tempCart = Cart.tempCart;

            if (!tempCart.containsKey(foodId)) {
                tempCart.put(foodId, new CartItem(food, 1));
            } else {
                if (change + tempCart.get(foodId).getFoodNum() <= 0) {
                    tempCart.remove(foodId);
                } else {
                    CartItem cartItem = tempCart.get(foodId);
                    cartItem.setFoodNum(cartItem.getFoodNum() + change);
                }
            }
            for (CartItem cartItem : tempCart.values()) {
                cartItems.add(cartItem);
            }
            session.setAttribute("cartItems", cartItems);
        } else {
            currentUser = (Member) currentUserObj;
            Cart cart = currentUser.getCart();
            cartItems = currentUser.getCart().getCartItems();
            //從購物車中尋找是否有該餐點
            CartItem cartItem = cartItemRepository.findByFood_FoodIdAndCart_CartId(foodId, cart.getCartId());
            if (cartItem == null) {
                CartItem tempCartItem = new CartItem
                        (currentUser.getCart(), food, 1);
                cartItems.add(tempCartItem);
                cartItemRepository.save(tempCartItem);
            } else {
                if (change + cartItem.getFoodNum() > 0) {
                    //可以對 cartItems 內部的 CartItem 修改值的
                    cartItems.stream().filter(cartItem1 -> cartItem1.getFood().getFoodId() == foodId)
                            .findFirst().ifPresent(cartItem1 -> {
                                int num = cartItem1.getFoodNum() + change;
                                cartItem1.setFoodNum(num);
                            });
                    CartItem item = new CartItem(cartItem.getCartItemId(), currentUser.getCart(), cartItem.getFood(), change + cartItem.getFoodNum());
                    cartItemRepository.save(item);
                } else {
                    cartItems.removeIf(item -> item.getFood().getFoodId() == foodId);
                    cartItemRepository.deleteById(cartItem.getCartItemId());
                }
            }
        }

    }

}
