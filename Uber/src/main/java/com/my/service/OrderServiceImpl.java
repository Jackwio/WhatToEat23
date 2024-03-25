package com.my.service;

import com.my.Enum.OrderState;
import com.my.Enum.RestOrderState;
import com.my.Repository.*;
import com.my.pojo.*;
import jakarta.jms.Destination;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RatingsRepository ratingsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private JmsTemplate jmsTemplate;
    @Autowired
    @Qualifier("orderQueue")
    private Destination orderQueue;
    @Autowired
    @Qualifier("informQueue")
    private Destination informQueue;
    @Autowired
    private RestTemplate restTemplate;

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
        //儲存訂單訊息
        order.setOrderItems(orderItems);
        orderRepository.save(order);
//        currentUser.getOrders().add(saveOrder);  //為什麼會需要初始化
        //刪除購物車並給他一個新的購物車
        Cart cart1 = new Cart(currentUser, 0, 0);
        cart1.setCartItems(new ArrayList<>());
        cartRepository.deleteById(cart.getCartId());
        cartRepository.save(cart1);
        //獲取使用者資料
        Member member = memberRepository.findById(currentUser.getMemEmail()).get();
        session.setAttribute("currentUser",member);
        jmsTemplate.send(orderQueue, session1 -> {
            Message message = session1.createObjectMessage(order);
            message.setStringProperty("source", "store");
            return message;
        });
        jmsTemplate.send(informQueue, session1 -> {
            Message message = session1.createObjectMessage(order);
            message.setStringProperty("inform", "下單了");
            return message;
        });
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

    @Transactional
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
