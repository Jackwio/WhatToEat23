package com.my.service;

import com.my.Enum.RestOrderState;
import com.my.pojo.Order;
import com.my.pojo.Restaurant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


public interface RestaurantBackService {

    List<Order> getOrders(HttpServletRequest request, HttpSession session);

    void acceptOrder(HttpServletRequest request, HttpSession session, Long orderId);

    void rejectOrder(HttpServletRequest request, HttpSession session, Long orderId);

    void getOrderByState(HttpServletRequest request, HttpSession session, RestOrderState... restOrderState);

    void finishOrder(HttpServletRequest request, HttpSession session, Long orderId);

    void changePhoto(HttpSession session, MultipartFile multipartFile);

    void changeName(HttpSession session, String restName);
    Map<String, Object> getInitialChartData(HttpSession session);
    void getComment(HttpSession session);

//    void editRestaurant(Restaurant restaurant, HttpSession session);
//    void registerRestaurant(MultipartFile multipartFile, Restaurant restaurant, HttpServletRequest request, String ownerEmail);
//
//    void registerOwner(MultipartFile multipartFile, Member member, Restaurant restaurant, String password, HttpServletRequest request);
//
//    Restaurant loginOwner(String ownerEmail, String password, HttpSession session);


//    void register(MultipartFile multipartFileMem, Member member, String password, MultipartFile multipartFileRest, Restaurant restaurant, HttpServletRequest request);
//
//
//

//
//    void getAverRating(Integer restId,HttpSession session);
//

}

