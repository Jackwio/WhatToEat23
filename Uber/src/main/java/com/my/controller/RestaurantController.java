package com.my.controller;

import com.my.pojo.Member;
import com.my.service.OrderService;
import com.my.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;


    public RestaurantController() {
    }

    @RequestMapping("/lookRestaurant")
    public String lookRestaurant(HttpSession session, Integer pageNumber, String keyword, String action, String position,
                                 Integer time, String foodType, String foodClass,
                                 Integer foodMoney, String foodConstraint, HttpServletRequest request) {

        restaurantService.lookRestaurant(session, pageNumber, keyword, action, position, time,
                foodType, foodClass, foodMoney, foodConstraint);
        if (session.getAttribute("currentUser") != null) {
            Member currentUser = (Member) session.getAttribute("currentUser");
            if (currentUser.getMemType() == 0) {
                orderService.getCommentOrder(request, session, currentUser);
            }
        }

        //回到main
        return "restaurant/main";
    }

    @RequestMapping("/useTurntable")
    public String useTurntable(HttpSession session) {
        restaurantService.useTurntable(session);
        return "restaurant/turntable";
    }

    @RequestMapping("/getTurnTableResult")
    public String getTurnTableResult(Integer target, HttpSession session) {
        Integer restId = restaurantService.getTurnTableResult(target, session);
        return "forward:/lookFood?restId=" + restId;
    }

}
