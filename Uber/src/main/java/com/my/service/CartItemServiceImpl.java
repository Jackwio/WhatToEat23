package com.my.service;

import com.my.Repository.CartItemRepository;
import com.my.Repository.CartRepository;
import com.my.Repository.FoodRepository;
import com.my.Repository.MemberRepository;
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
import java.util.stream.Collectors;

@Service
public class CartItemServiceImpl implements CartItemService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private MemberRepository memberRepository;

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
        List<CartItem> cartItems;
        Food food = foodRepository.findById(foodId).get();

        Object currentUserObj = session.getAttribute("currentUser");

        if (currentUserObj == null) {
            Cart tempCart = Cart.tempCart;
            cartItems = tempCart.getCartItems();
            CartItem cartItem = tempCart.getCartItems().stream()
                    .filter(item -> item.getFood().equals(food))
                    .findFirst().orElseGet(() -> {
                        return null;
                    });
            if (cartItem == null) {
                cartItems.add(new CartItem(food, change));
            } else {
                if (change + cartItem.getFoodNum() <= 0) {
                    cartItems.removeIf(cartItem1 -> cartItem1.getFood().equals(food));
                } else {
                    cartItems.stream().filter(cartItem1 -> cartItem1.getFood().equals(food))
                            .findFirst().ifPresent(cartItem1 -> {
                                cartItem1.setFoodNum(cartItem.getFoodNum() + change);
                            });
                }
            }
            tempCart.setCartItemTotal(tempCart.getCartItemTotal() + change * food.getFoodPrice());
            tempCart.setItemNum(cartItems.size());
            session.setAttribute("tempCart", tempCart);
        } else {
            currentUser = (Member) currentUserObj;
            Cart cart = currentUser.getCart();
            cartItems = cart.getCartItems();
            //從購物車中尋找是否有該餐點
            CartItem cartItem = cartItems.stream().filter(item -> item.getFood().equals(food))
                    .findFirst().orElseGet(() -> {
                        return null;
                    });
            CartItem tempCartItem;
            if (cartItem == null) {
                tempCartItem = new CartItem
                        (currentUser.getCart(), food, change);
                cartItems.add(tempCartItem);
            } else {
                if (change + cartItem.getFoodNum() > 0) {
                    //可以對 cartItems 內部的 CartItem 修改值的
                    cartItems.stream().filter(cartItem1 -> cartItem1.getFood().equals(food))
                            .findFirst().ifPresent(cartItem1 -> {
                                cartItem1.setFoodNum(cartItem1.getFoodNum() + change);
                            });
                } else {
                    cartItems.removeIf(item -> item.getFood().equals(food));
                }
            }
            cart.setCartItemTotal(cart.getCartItemTotal() + food.getFoodPrice() * change);
            cart.setItemNum(cartItems.size());
            cartRepository.save(cart);
            Member member = memberRepository.findById(currentUser.getMemEmail()).get();
            session.setAttribute("currentUser", member);
        }

    }

}
