package com.my.service;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

public interface FoodService {
    void lookFood(Integer restId,String keyword, HttpSession session);

    Integer addFood(MultipartFile multipartFile, String foodName, Integer foodPrice, String menuFoodType, HttpSession session);

    Integer editFood(HttpSession session, MultipartFile multipartFile, String foodName, Integer foodPrice, String foodType, Integer foodId);

    void deleteFood(HttpSession session, Integer foodId);

    void searchFood(String foodName, Integer restId, HttpSession session);
}
