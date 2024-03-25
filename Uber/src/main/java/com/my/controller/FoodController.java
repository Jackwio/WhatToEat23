package com.my.controller;

import com.my.service.FoodService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/food")
public class FoodController {

    @Autowired
    private FoodService foodService;


    public FoodController() {
    }


    @ResponseBody
    @RequestMapping("/editFood")
    public String editFood(@RequestPart(value = "foodPhoto", required = false) MultipartFile multipartFile, String foodName,
                           Integer foodPrice, String foodType, Integer foodId, HttpSession session) {
        Integer i = foodService.editFood(session, multipartFile, foodName, foodPrice, foodType, foodId);
        if (i == 1) {
            return "餐點修改成功";
        }
        return "餐點修改失敗";
    }

    @RequestMapping("/deleteFood")
    public String deleteFood(HttpSession session, Integer foodId) {
        foodService.deleteFood(session, foodId);
        return "redirect:/goToFoodsOfRest";
    }

    @ResponseBody
    @RequestMapping("/addFood")
    public String addFood(@RequestPart("foodPhoto") MultipartFile multipartFile, String foodName,
                          Integer foodPrice, String foodType, HttpSession session) {
        Integer i = foodService.addFood(multipartFile, foodName, foodPrice, foodType, session);
        if (i == 1) {
            return "增加餐點成功";
        }
        return "名稱重複，增加餐點失敗";
    }

}
