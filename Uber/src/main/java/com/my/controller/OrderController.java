package com.my.controller;

import com.my.service.OrderService;
import com.my.service.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController() {
    }

    //準備付款，顯示詳細訂單項目
    @RequestMapping("/goToPay")
    public String goToPay(HttpSession session, HttpServletRequest request) {
        orderService.goToPay(session, request);
        return "order/cart";
    }

    //下單完成
    @RequestMapping("/order")
    public String Order(HttpSession session) {
        orderService.order(session);
        return "restaurant/main";
    }
}
