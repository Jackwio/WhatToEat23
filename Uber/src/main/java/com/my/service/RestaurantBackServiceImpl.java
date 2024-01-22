package com.my.service;

import com.my.Enum.OrderState;
import com.my.Enum.RestOrderState;
import com.my.Repository.OrderRepository;
import com.my.Repository.RatingsRepository;
import com.my.Repository.RestaurantRepository;
import com.my.pojo.Order;
import com.my.pojo.Ratings;
import com.my.pojo.Restaurant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class RestaurantBackServiceImpl implements RestaurantBackService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private RatingsRepository ratingsRepository;

    //0:未接單
    //1:拒絕訂單
    //2:接收訂單
    //3:完成訂單
    @Override //取得訂單
    public List<Order> getOrders(HttpServletRequest request, HttpSession session) {
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        List<Order> orders = orderRepository.findAllByRestaurant_RestId(currentRestaurant.getRestId());
        request.setAttribute("orders", orders);
        return orders;
    }

    @Override //拒絕訂單
    public void rejectOrder(HttpServletRequest request, HttpSession session, Long orderId) {
        Optional<Order> o = orderRepository.findById(orderId);
        Order order = o.get();
        order.setRestAccepted(RestOrderState.REJECT);
        order.setOrderState(OrderState.REJECTEDBYREST);
        orderRepository.save(order);
    }

    @Override
    public void acceptOrder(HttpServletRequest request, HttpSession session, Long orderId) {
        Optional<Order> o = orderRepository.findById(orderId);
        Order order = o.get();
        order.setRestAccepted(RestOrderState.ACCEPTED);
        orderRepository.save(order);
    }

    @Override
    public void finishOrder(HttpServletRequest request, HttpSession session, Long orderId) {
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        Optional<Order> o = orderRepository.findById(orderId);
        Order order = o.get();
        order.setRestAccepted(RestOrderState.FINISH);
        order.setOrderState(OrderState.FINISH);
        orderRepository.save(order);
        Integer orderAllNumbers = (Integer) session.getAttribute("orderAllNumbers");
        Integer orderAllRevenues = (Integer) session.getAttribute("orderAllRevenues");
        orderAllRevenues += order.getOrderTotalPrice();
        session.setAttribute("orderAllNumbers", ++orderAllNumbers);
        session.setAttribute("orderAllRevenues", orderAllRevenues);
        currentRestaurant.setSales(orderAllRevenues);
        currentRestaurant.setOrdersNum(orderAllNumbers);
        restaurantRepository.save(currentRestaurant);
    }

    @Override
    public void getOrderByState(HttpServletRequest request, HttpSession session, RestOrderState... restOrderState) {
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        List<Order> orders = orderRepository.findAllByRestaurant_RestIdAndRestAccepted(currentRestaurant.getRestId(), restOrderState[0]);
        if (restOrderState.length == 2) {
            orders.addAll(orderRepository.findAllByRestaurant_RestIdAndRestAccepted(currentRestaurant.getRestId(), restOrderState[1]));
        }
        request.setAttribute("orders", orders);
    }

    @Override
    public void changePhoto(HttpSession session, MultipartFile multipartFile) {
        //獲取當前資料夾路徑
        String directoryName = System.getProperty("user.dir");
        //拼出路徑並儲存到當前專案的static的images下
        String uPhotoPrefix = "/static/images/";
        String uPhotoPath;
        if (multipartFile == null) {
            uPhotoPath = null;
        } else {
            uPhotoPath = uPhotoPrefix + multipartFile.getOriginalFilename();
            File file = new File(directoryName + "/Uber/src/main/resources/webapp" + uPhotoPath);
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        currentRestaurant.setRestPhoto(uPhotoPath);
        restaurantRepository.save(currentRestaurant);
    }

    @Override
    public void changeName(HttpSession session, String restName) {
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        currentRestaurant.setRestName(restName);
        restaurantRepository.save(currentRestaurant);
    }

//    @Override //註冊餐廳
//    public void registerRestaurant(MultipartFile multipartFile, Restaurant restaurant, HttpServletRequest request, String ownerEmail) {
////        獲取當前資料夾路徑
//        String directoryName = System.getProperty("user.dir");
////        拼出路徑並儲存到當前專案的static的images下
//        String uPhotoPrefix = "/static/images/";
//        String uPhotoPath = uPhotoPrefix + multipartFile.getOriginalFilename();
//        File file = new File(directoryName + "/Uber/src/main/resources/webapp" + uPhotoPath);
//        try {
//            multipartFile.transferTo(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //從資料庫檢查有沒有重複餐廳餐廳(名字)
//        LambdaQueryWrapper<Restaurant> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Restaurant::getRestName, restaurant.getRestName());
//        Restaurant tempRestaurant = restaurantMapper.selectOne(queryWrapper);
//        if (tempRestaurant == null) {
//            //如果沒有重複新增餐廳到資料庫
//            restaurantMapper.insert(new Restaurant(null, restaurant.getRestName(), restaurant.getRestLocation(), restaurant.getRestPrice(),
//                    restaurant.getRestOpenTime(), restaurant.getRestCloseTime(), restaurant.getRestFoodType()
//                    , restaurant.getRestDietConstraint(), 0.0, uPhotoPath, 0, 0, 0, ownerEmail));
//        } else {
//            throw new RuntimeException();
//        }
//    }
//
//    @Override //註冊餐廳擁有者
//    public void registerOwner(MultipartFile multipartFile, Member member, Restaurant restaurant, String password, HttpServletRequest request) {
//
////        獲取當前資料夾路徑
//        String directoryName = System.getProperty("user.dir");
////        拼出路徑並儲存到當前專案的static的images下
//        String uPhotoPrefix = "/static/images/";
//        String uPhotoPath = uPhotoPrefix + multipartFile.getOriginalFilename();
//        File file = new File(directoryName + "/Uber/src/main/resources/webapp" + uPhotoPath);
//        try {
//            multipartFile.transferTo(file);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //從資料庫檢查有沒有重複餐廳擁有者(Email OR 電話)
//        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Member::getMemEmail, member.getMemEmail()).or().eq(Member::getMemPhoneNum, member.getMemPhoneNum());
//        List<Member> tempOwner = memberMapper.selectList(queryWrapper);
//        if (tempOwner.size() == 0) {
//            //如果沒有重複新增餐廳擁有者到資料庫
//            memberMapper.addMember(new Member(member.getMemEmail(), member.getMemName(), member.getMemPhoneNum(), 1, uPhotoPath, null));
//            memPasswordMapper.addPassword(new MemPassword(member, password));
//        } else {
//            throw new RuntimeException();
//        }
//    }
//
//    @Override //餐廳擁有者登入
//    public Restaurant loginOwner(String ownerEmail, String password, HttpSession session) {
//        //透過email去尋找餐廳擁有者
//        Restaurant restaurant = null;
//        LambdaQueryWrapper<Member> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Member::getMemEmail, ownerEmail);
//        Member tempOwner = memberMapper.selectOne(queryWrapper);
//        if (tempOwner == null) {
//            return null;
//        }
//        //如果找的到，繼續判斷密碼是否正確，若正確，把當前使用者保存在session裡
//        MemPassword memPassword = memPasswordMapper.selectByEmail(ownerEmail);
//        if (memPassword.getPassword().equals(password)) {
//            session.setAttribute("currentUser", tempOwner);
//            //透過用戶id找到餐廳
//            LambdaQueryWrapper<Restaurant> queryWrapper2 = new LambdaQueryWrapper<>();
//            queryWrapper2.eq(Restaurant::getOwnerEmail, tempOwner.getMemEmail());
//            restaurant = restaurantMapper.selectOne(queryWrapper2);
//        }
//        return restaurant;
//    }


//    @Transactional
//    @Override
//    public void register(MultipartFile multipartFileMem, Member member, String password, MultipartFile multipartFileRest, Restaurant restaurant, HttpServletRequest request) {
//        try {
//            registerOwner(multipartFileMem, member, restaurant, password, request);
//            registerRestaurant(multipartFileRest, restaurant, request, member.getMemEmail());
//        } catch (Exception e) {
//            throw e;
//        }
//    }
//

//

    //
    @Override
    public void getComment(HttpSession session) {
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        List<Ratings> ratingsList = ratingsRepository.findAllByOrderId_Restaurant_RestIdAndRatingsContentIsNotNull(currentRestaurant.getRestId());
        session.setAttribute("ratingsList", ratingsList);
    }


    @Override
    public Map<String, Object> getInitialChartData(HttpSession session) {
        int[] orderNumbers = new int[12];
        int[] orderRevenue = new int[12];
        HashMap<String, Object> map = new HashMap<>();
        Restaurant currentRestaurant = (Restaurant) session.getAttribute("currentRestaurant");
        List<Order> orders = orderRepository.findAllByRestaurant_RestIdAndRestAccepted(currentRestaurant.getRestId(), RestOrderState.FINISH);
        for (Order order : orders) {
            int day = order.getOrderDateTime().getDayOfMonth() - 16;
            orderNumbers[day] += 1;
            orderRevenue[day] += order.getOrderTotalPrice();
        }
        map.put("orderNumbers", orderNumbers);
        map.put("orderRevenue", orderRevenue);
        return map;
    }
}