package com.my.controller;

import com.my.Enum.RestOrderState;
import com.my.pojo.Member;
import com.my.pojo.Order;
import com.my.pojo.Restaurant;
import com.my.service.FoodService;
import com.my.service.RestaurantBackService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@Controller
public class RestaurantBackController {
    @Autowired
    private RestaurantBackService restaurantBackService;
    @Autowired
    private FoodService foodService;

    public RestaurantBackController() {
    }

    @RequestMapping("/getOrders")
    public String getOrders(HttpServletRequest request, HttpSession session) {
        List<Order> orders = restaurantBackService.getOrders(request, session);
        return "restaurantBack/orderManage";
    }

    //拒絕會員訂單
    @RequestMapping("/rejectOrder")
    public String rejectOrder(HttpServletRequest request, HttpSession session, Long orderId) {
        restaurantBackService.rejectOrder(request, session, orderId);
        //回到main
        return "forward:getOrderNotReceiveAndFinish";
    }

    //取得未接單的訂單或餐廳未完成
    @RequestMapping("/getOrderNotReceiveAndFinish")
    public String getOrderNotReceiveAndFinish(HttpServletRequest request, HttpSession session) {
        restaurantBackService.getOrderByState(request, session, RestOrderState.UNACCEPT, RestOrderState.ACCEPTED);
        return "restaurantBack/receiveOrder";
    }

    //接收會員訂單
    @RequestMapping("/acceptOrder")
    public String acceptOrder(HttpServletRequest request, HttpSession session, Long orderId) {
        restaurantBackService.acceptOrder(request, session, orderId);
        //回到main
        return "forward:getOrderNotReceiveAndFinish";
    }

    @RequestMapping("/finishOrder")
    public String finishOrder(Long orderId, HttpServletRequest request, HttpSession session) {
        restaurantBackService.finishOrder(request, session, orderId);
        return "forward:getOrderNotReceiveAndFinish";
    }

    @RequestMapping("/goToFoodsOfRest")
    public String goToFoodsOfRest() {
        return "restaurantBack/menu";
    }

    //    @RequestMapping("/editRestaurant")
//    public String editRestaurant(HttpSession session, Restaurant restaurant) {
//        restaurantBackService.editRestaurant(restaurant, session);
//        //回到main
//        return "restaurantBack/restaurant";
//    }
//
//    @ResponseBody
//    @RequestMapping("/registerOwner")
//    public String registerOwner(@RequestParam("memPhoto") MultipartFile multipartFileMem, String memEmail,
//                                String memPhoneNum, String password
//            , @RequestParam("restPhoto") MultipartFile multipartFileRest, String restName, String restLocation,
//                                Integer restPrice, Integer restOpenTime, Integer restCloseTime, String restFoodType, String restDietConstraint, HttpServletRequest request) {
//
//        Member member = new Member();
//        member.setMemPhoneNum(memPhoneNum);
//        member.setMemEmail(memEmail);
//        Restaurant restaurant = new Restaurant(restName, restLocation, restPrice, restOpenTime, restCloseTime, restFoodType, restDietConstraint, 0);
//        try {
//            restaurantBackService.register(multipartFileMem, member, password, multipartFileRest, restaurant, request);
//        } catch (Exception e) {
//            return "註冊失敗";
//        }
//        //若唯一，則儲存，並回到index頁面
//        return "註冊成功";
//
//    }
//
//    @RequestMapping("/loginOwner")
//    public String loginOwner(String ownerEmail, String password, HttpSession session) {
//        Restaurant tempRestaurant = restaurantBackService.loginOwner(ownerEmail, password, session);
//        if (tempRestaurant != null) {
//            return "restIndex";
//        }
//        return "login";
//    }
//
    @ResponseBody
    @RequestMapping(value = "/changePhoto")
    public String changePhoto(HttpSession session, @RequestPart(value = "image", required = false) MultipartFile multipartFile, @RequestParam(value = "restName", required = false) String restName) {
        if (multipartFile != null) {
            restaurantBackService.changePhoto(session, multipartFile);
        }
        if (restName != null) {
            restaurantBackService.changeName(session, restName);
        }
        return "變動成功";
    }

    @RequestMapping("/getComment")
    public String getComment(HttpSession session) {
        restaurantBackService.getComment(session);
        return "restaurantBack/comment";
    }

    @ResponseBody
    @RequestMapping("/getInitialChartData")
    public Map<String, Object> getInitialChartData(HttpSession session){
        Map<String, Object> initialChartData = restaurantBackService.getInitialChartData(session);
        return initialChartData;
    }

    @RequestMapping("/restBackLogOut")
    public String restBackLogOut(HttpSession session) {
        session.removeAttribute("currentUser");
        session.removeAttribute("currentRestaurant");
        return "user/index";
    }

}

