package com.my.service;

import com.my.Repository.FoodRepository;
import com.my.Repository.RestaurantRepository;
import com.my.pojo.Food;
import com.my.pojo.Restaurant;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;

    @Transactional
    @Override
    public void lookFood(Integer restId,String keyword, HttpSession session) {

        //查詢餐點資料
        List<Food> foods = foodRepository.findAllByRestaurant_RestIdAndFoodNameContaining(restId,keyword);
        //查詢餐廳資料
        Restaurant restaurant = restaurantRepository.findByRestId(restId);

        session.setAttribute("restaurant", restaurant);
        session.setAttribute("foods", foods);

        List<String> types = foodRepository.findDistinctFoodTypeByRestaurant_RestId(restId);
        session.setAttribute("types", types);
    }

    @Override
    public void searchFood(String foodName,Integer restId, HttpSession session) {
        List<Food> foods = foodRepository.findAllByRestaurant_RestIdAndFoodNameContaining(restId,foodName);
        session.setAttribute("foods",foods);
    }

    @Override
    public Integer addFood(MultipartFile multipartFile, String foodName, Integer foodPrice, String foodType, HttpSession session) {
        //獲取當前資料夾路徑
        String directoryName = System.getProperty("user.dir");
        //拼出路徑並儲存到當前專案的static的images下
        String uPhotoPrefix = "/static/images/";
        String uPhotoPath = uPhotoPrefix + multipartFile.getOriginalFilename();
        File file = new File(directoryName + "/Uber/src/main/resources/webapp" + uPhotoPath);
        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        Food existFood = foodRepository.findByFoodNameAndRestaurant_RestId(foodName, currentRestaurant.getRestId());
        if (existFood == null) {
            foodRepository.save(new Food(foodName, foodPrice, uPhotoPath, foodType, currentRestaurant));
            Restaurant restaurant = restaurantRepository.findByRestId(currentRestaurant.getRestId());
            session.setAttribute("currentRestaurant", restaurant);
            return 1;
        }
        return 0;
    }

    @Override
    public Integer editFood(HttpSession session, MultipartFile multipartFile, String foodName, Integer foodPrice, String foodType, Integer foodId) {

        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        Food existFood = foodRepository.findByFoodId(foodId);
        String uPhotoPath = existFood.getFoodPhoto();

        if (existFood.getFoodName().equals(foodName) && existFood.getFoodId() != foodId) {
            return 0;
        }
        if (multipartFile != null) {
            String directoryName = System.getProperty("user.dir");
            //拼出路徑並儲存到當前專案的static的images下
            String uPhotoPrefix = "/static/images/";
            uPhotoPath = uPhotoPrefix + multipartFile.getOriginalFilename();
            File file = new File(directoryName + "/Uber/src/main/resources/webapp" + uPhotoPath);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Food food = new Food(foodId, foodName, foodPrice, uPhotoPath, foodType, currentRestaurant);
        foodRepository.save(food);
        Restaurant restaurant = restaurantRepository.findByRestId(currentRestaurant.getRestId());
        session.setAttribute("currentRestaurant", restaurant);
        return 1;
    }

    @Override
    public void deleteFood(HttpSession session,Integer foodId) {
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        foodRepository.deleteById(foodId);
        Restaurant restaurant = restaurantRepository.findByRestId(currentRestaurant.getRestId());
        session.setAttribute("currentRestaurant", restaurant);
    }
}
