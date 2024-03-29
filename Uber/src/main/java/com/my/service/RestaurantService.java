package com.my.service;

import com.my.pojo.Ratings;
import com.my.pojo.Restaurant;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RestaurantService{

    void lookRestaurant(HttpSession session, Integer pageNumber, String keyword, String action, Integer time, String foodClass,Restaurant restaurant);
    List<Restaurant> selectRestaurantByTime(HttpSession session,List<Restaurant> restaurants, Integer time );
    List<Restaurant> selectRestaurantByKeyword(HttpSession session,List<Restaurant> restaurants, String keyword);
    List<Restaurant> selectRestaurantByPosition(HttpSession session,List<Restaurant> restaurants, String position);
    void selectRestaurantByFoodType(HttpSession session,List<Restaurant> restaurants, String foodType);
    void selectRestaurantByFoodMoney(HttpSession session,List<Restaurant> restaurants, Integer foodMoney);
    void selectRestaurantByFoodConstraint(HttpSession session,List<Restaurant> restaurants, String foodConstraint);

    void useTurntable(HttpSession session);
    Integer getTurnTableResult(Integer target, HttpSession session);

    void updateAverageRating(HttpSession session, Ratings ratings);
}
