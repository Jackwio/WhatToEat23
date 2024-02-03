package com.my.service;

import com.my.Enum.OrderState;
import com.my.Enum.RestOrderState;
import com.my.Repository.*;
import com.my.pojo.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private RatingsRepository ratingsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    @Override
    public void order(HttpSession session) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        List<OrderItem> orderItems = new ArrayList<>();
        Cart cart = currentUser.getCart();
        List<CartItem> cartItems = cart.getCartItems();
        Integer total = (Integer) session.getAttribute("total");
        //當前日期
        LocalDateTime tempNowTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd-HH:mm:ss");
        String text = tempNowTime.format(formatter);
        LocalDateTime nowTime = LocalDateTime.parse(text, formatter);
        //儲存訂單資料，並找到該order的id
        Restaurant restaurant = restaurantRepository.findById(cartItems.get(0).getFood().getRestaurant()
                .getRestId()).get();
        Order order = new Order(total, cartItems.size(), nowTime, currentUser, restaurant,
                RestOrderState.UNACCEPT, OrderState.PAID);
        //儲存訂單項目詳情
        for (CartItem cartItem : cartItems) {
            orderItems.add(new OrderItem(order, cartItem.getFood(), cartItem.getFoodNum(),
                    cartItem.getFoodNum() * cartItem.getFood().getFoodPrice()));
        }
        order.setOrderItems(orderItems);
        orderRepository.save(order);
        cart.setItemNum(0);
        cart.setCartItemTotal(0);
//        cartItems.clear();
//        cartRepository.save(cart);
    }

    @Override
    public void goToPay(HttpSession session, HttpServletRequest request) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        Integer total = calculateAll(currentUser.getCart().getCartItems());
        session.setAttribute("total", total);
    }

    @Override
    public Integer calculateAll(List<CartItem> cartItems) {
        int total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getFood().getFoodPrice() * cartItem.getFoodNum();
        }
        return total;
    }

    @Override
    public void getCommentOrder(HttpServletRequest request, HttpSession session, Member member) {
        List<Long> ratingsOrderId = ratingsRepository.findOrderIdByMember_MemEmail(member.getMemEmail());
        List<Order> commentOrders = orderRepository.findAllByMember_MemEmailAndRestAccepted(member.getMemEmail(), RestOrderState.FINISH);
        commentOrders = commentOrders.stream().filter(order -> !ratingsOrderId.contains(order.getOrderId())).collect(Collectors.toList());
        if (commentOrders.size() != 0) {
            session.setAttribute("currentCommentOrder", commentOrders);
        } else {
            session.removeAttribute("currentCommentOrder");
        }
    }
}
