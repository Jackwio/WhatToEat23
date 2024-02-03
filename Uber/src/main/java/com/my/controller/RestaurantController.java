package com.my.controller;

import com.my.pojo.Member;
import com.my.pojo.Restaurant;
import com.my.service.OrderService;
import com.my.service.RestaurantService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/restaurant")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private OrderService orderService;


    public RestaurantController() {
    }

    @GetMapping("/")
    public String lookRestaurant(HttpSession session, Integer pageNumber, String keyword, String action,
                                 Integer time, String foodClass,
                                 Restaurant restaurant, HttpServletRequest request) {

        restaurantService.lookRestaurant(session, pageNumber, keyword, action, time, foodClass,restaurant);
        if (session.getAttribute("currentUser") != null) {
            Member currentUser = (Member) session.getAttribute("currentUser");
            if (currentUser.getMemType() == 0) {
                orderService.getCommentOrder(request, session, currentUser);
            }
        }

        //回到main
        return "restaurant/main";
    }

    @GetMapping("/turntable")
    public String useTurntable(HttpSession session) {
        restaurantService.useTurntable(session);
        return "restaurant/turntable";
    }

    @GetMapping("/turntable/{target}")
    public String getTurnTableResult(@PathVariable Integer target, HttpSession session) {
        Integer restId = restaurantService.getTurnTableResult(target, session);
        return "forward:/lookFood?restId=" + restId;
    }

}
