package com.my.service;

import com.my.pojo.Restaurant;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RestaurantService{

    void lookRestaurant(HttpSession session, Integer pageNumber, String keyword, String action, String position, Integer time, String foodType, String foodClass, Integer foodMoney, String foodConstraint);

//    List<Restaurant> selectRestaurantByTime(List<Restaurant> restaurants, Integer time );
    List<Restaurant> selectRestaurantByKeyword(HttpSession session,List<Restaurant> restaurants, String keyword);
//
    List<Restaurant> selectRestaurantByPosition(HttpSession session,List<Restaurant> restaurants, String position);
    void selectRestaurantByFoodType(HttpSession session,List<Restaurant> restaurants, String foodType);
    void selectRestaurantByFoodMoney(HttpSession session,List<Restaurant> restaurants, Integer foodMoney);
    void selectRestaurantByFoodConstraint(HttpSession session,List<Restaurant> restaurants, String foodConstraint);

//    Long getRestaurantNumber(HttpSession session, List<Restaurant> restaurants);
    void useTurntable(HttpSession session);
    Integer getTurnTableResult(Integer target, HttpSession session);

    void updateAverageRating(HttpSession session,Long orderId,Integer ratingStar);
}
