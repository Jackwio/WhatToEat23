package com.my.controller;

import com.my.service.OrderService;
import com.my.service.OrderServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    public OrderController() {
    }

    //準備付款，顯示詳細訂單項目
    @GetMapping("/")
    public String goToPay(HttpSession session, HttpServletRequest request) {
        orderService.goToPay(session, request);
        return "order/pay";
    }

    //下單完成
    @ResponseBody
    @PutMapping("/payment")
    public ResponseEntity<Map<String,String>> Order(HttpSession session) {
        HashMap<String, String> map = new HashMap<>();
        map.put("message","付款成功");
        orderService.order(session);
        return ResponseEntity.ok(map);
    }
}
