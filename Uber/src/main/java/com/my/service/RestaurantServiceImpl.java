package com.my.service;

import com.my.Enum.OrderState;
import com.my.Repository.OrderRepository;
import com.my.Repository.RatingsRepository;
import com.my.Repository.RestaurantRepository;
import com.my.pojo.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RatingsRepository ratingsRepository;

    @Override
    public void lookRestaurant(HttpSession session, Integer pageNumber, String keyword, String action,
                               Integer time, String foodClass, Restaurant restaurant) {
        String position = restaurant.getRestLocation();
        String foodType = restaurant.getRestFoodType();
        Integer foodMoney = restaurant.getRestPrice();
        String foodConstraint = restaurant.getRestDietConstraint();
        //判斷是訪客還是會員
        if (session.getAttribute("currentUser") == null) {
            //在本地紀錄訪客資訊
            Cart.tempCart = new Cart();
            ArrayList<CartItem> cartItems = new ArrayList<>();
            Cart.tempCart.setCartItemTotal(0);
            Cart.tempCart.setCartItems(cartItems);
            session.setAttribute("tempCart", Cart.tempCart);
        }
        //查詢所有餐廳
        List<Restaurant> restaurants = restaurantRepository.findAll();
        if (pageNumber == null) {
            pageNumber = 1;
        }
        //判斷是搜尋還是瀏覽
        if ("search".equals(action)) {
            if (keyword.equals("")) {
                session.removeAttribute("foodType");
                session.removeAttribute("foodMoney");
                session.removeAttribute("foodClass");
                session.removeAttribute("foodConstraint");
            }
        }
        //位置篩選
        if ((position != null || session.getAttribute("position") != null) && !"".equals(position)) {
            restaurants = selectRestaurantByPosition(session, restaurants, position);
        }

        //時間搜尋
        if (time != null || session.getAttribute("time") != null) {
            restaurants = selectRestaurantByTime(session, restaurants, time);
        }

        //關鍵字搜尋
        if (keyword != null || session.getAttribute("keyword") != null) {
            restaurants = selectRestaurantByKeyword(session, restaurants, keyword);
        }
        //篩選食物種類
        if (foodType != null || session.getAttribute("foodType") != null) {
            selectRestaurantByFoodType(session, restaurants, foodType);
        }
        //篩選價錢
        if (foodMoney != null && foodMoney == 0) {
            session.removeAttribute("foodMoney");
        } else {
            if (foodMoney != null || session.getAttribute("foodMoney") != null) {
                selectRestaurantByFoodMoney(session, restaurants, foodMoney);
            }
        }
        //篩選食物限制
        if (("").equals(foodConstraint)) {
            session.removeAttribute("foodConstraint");
        } else {
            if (foodConstraint != null || session.getAttribute("foodConstraint") != null) {
                selectRestaurantByFoodConstraint(session, restaurants, foodConstraint);
            }
        }

        //配置分頁
        List<Integer> restaurantIds = restaurants.stream().map(r -> r.getRestId()).collect(Collectors.toList());
        Sort sort = Sort.by("restName").descending();
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 3, sort);
        Page<Restaurant> restaurantPage;

        if (foodClass != null || session.getAttribute("foodClass") != null) {
            restaurantPage = restaurantRepository.findAllByRestIdInOrderByRestRatingsDesc(restaurantIds, pageRequest);
        } else {
            restaurantPage = restaurantRepository.findAllByRestIdIn(restaurantIds, pageRequest);
        }

        if (foodClass != null) {
            session.setAttribute("foodClass", foodClass);
        }
        session.setAttribute("pageNumber", pageNumber);
        session.setAttribute("maxPageNumber", restaurantPage.getTotalPages());
        session.setAttribute("restaurants", restaurantPage.getContent());
    }

    //搜尋
    @Override
    public List<Restaurant> selectRestaurantByKeyword(HttpSession session, List<Restaurant> restaurants, String keyword) {
        //Lambda 表達式允許使用未明確宣告為 final 的局部變數，只要它們是有效最終的(return語句前面都沒有更改到該變數值)，並且在 Lambda 表達式內部不被修改。
        String oldKeyword = (String) session.getAttribute("keyword");
        if (keyword != null) {
            session.setAttribute("keyword", keyword);
            return restaurants.stream().filter(restaurant -> restaurant.getRestName().contains(keyword)).collect(Collectors.toList());
        } else {
            return restaurants.stream().filter(restaurant -> restaurant.getRestName().contains(oldKeyword)).collect(Collectors.toList());
        }
    }


    @Override
    public List<Restaurant> selectRestaurantByTime(HttpSession session, List<Restaurant> restaurants, Integer time) {

        if (time != null) {
            session.setAttribute("time", time);
        } else {
            time = (Integer) session.getAttribute("time");
        }
        Integer t = time;
        return restaurants.stream().filter(restaurant ->
                        t >= restaurant.getRestOpenTime() && t < restaurant.getRestCloseTime())
                .collect(Collectors.toList());
    }

    @Override
    public List<Restaurant> selectRestaurantByPosition(HttpSession session, List<Restaurant> restaurants, String position) {

        String tempPosition = (String) session.getAttribute("position");
        if (position != null) {
            session.setAttribute("position", position);
            tempPosition = position;
        }
        String temp = tempPosition;
        return restaurants.stream().filter(restaurant -> restaurant.getRestLocation().equals(temp)).collect(Collectors.toList());
    }

    @Override
    public void selectRestaurantByFoodType(HttpSession session, List<Restaurant> restaurants, String foodType) {
        if (foodType != null) {
            session.setAttribute("foodType", foodType);
        } else {
            foodType = (String) session.getAttribute("foodType");
        }
        String temp = foodType;
        restaurants.removeIf(restaurant -> !restaurant.getRestFoodType().equals(temp));
    }

    @Override
    public void selectRestaurantByFoodMoney(HttpSession session, List<Restaurant> restaurants, Integer foodMoney) {
        if (foodMoney != null) {
            session.setAttribute("foodMoney", foodMoney);
        } else {
            foodMoney = (Integer) session.getAttribute("foodMoney");
        }
        Integer temp = foodMoney;
        restaurants.removeIf(restaurant -> !restaurant.getRestPrice().equals(temp));
    }

    @Override
    public void selectRestaurantByFoodConstraint(HttpSession session, List<Restaurant> restaurants, String foodConstraint) {
        if (foodConstraint != null) {
            session.setAttribute("foodConstraint", foodConstraint);
        } else {
            foodConstraint = (String) session.getAttribute("foodConstraint");
        }
        String temp = foodConstraint;
        restaurants.removeIf(restaurant -> !restaurant.getRestDietConstraint().equals(temp));
    }

    @Override
    public void useTurntable(HttpSession session) {

        long restNumbers = restaurantRepository.count();
        ArrayList<Integer> restIds = new ArrayList<>();
        ArrayList<Restaurant> turntableRests = new ArrayList<>();

        while (restIds.size() <= 8) {
            int randomNumber = (int) (Math.random() * Math.toIntExact(restNumbers)) + 1;
            while (restIds.contains(randomNumber)) {
                randomNumber = (int) (Math.random() * Math.toIntExact(restNumbers)) + 1;
            }
            Restaurant restaurant = restaurantRepository.findByRestIdAndIsDeleted(randomNumber, 0);
            if (restaurant != null) {
                restIds.add(randomNumber);
                turntableRests.add(restaurant);
            }
        }
        session.setAttribute("turntableRests", turntableRests);
    }

    @Override
    public Integer getTurnTableResult(Integer target, HttpSession session) {
        List<Restaurant> turntableRests = (List<Restaurant>) session.getAttribute("turntableRests");
        Restaurant restaurant = turntableRests.get(target);
        return restaurant.getRestId();
    }

    @Override
    public void updateAverageRating(HttpSession session, Long orderId, Integer star) {
        Optional<Order> o = orderRepository.findById(orderId);
        Order order = o.get();
        Restaurant restaurant = order.getRestaurant();
        List<Ratings> ratings = ratingsRepository.findAllByOrderId_Restaurant_RestIdAndRatingsContentIsNotNull(restaurant.getRestId());
        String formattedResult = String.format("%.1f", (ratings.size() * restaurant.getRestRatings() + star) / (ratings.size() + 1));
        restaurant.setRestRatings(Double.parseDouble(formattedResult));
        restaurantRepository.save(restaurant);
    }
}
