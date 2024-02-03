package com.my.service;

import com.my.Enum.OrderState;
import com.my.Enum.RestOrderState;
import com.my.Repository.*;
import com.my.pojo.*;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private AdminPasswordRepository adminPasswordRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private MemberPasswordRepository memberPasswordRepository;


    @Override
    public Admin loginAdmin(Map<String, String> adminEmailPasswordMap, HttpSession session) {
        String adminEmail = adminEmailPasswordMap.get("adminEmail");
        String password = adminEmailPasswordMap.get("password");
        Admin admin = adminRepository.findById(adminEmail).get();
        AdminPassword adminPassword = adminPasswordRepository.findByAdminEmail(admin.getAdminEmail());
        if (adminPassword.getPassword().equals(password)) {
            session.setAttribute("currentAdmin", admin);
        }else{
            throw new RuntimeException();
        }
        return admin;
    }

    @Override
    public Admin registerAdmin(HttpSession session, String adminEmail, String password) {
        Admin admin;
        try {
            admin = adminRepository.findById(adminEmail).get();
            return null;
        } catch (Exception e) {
            admin = new Admin(adminEmail);
            adminRepository.save(admin);
            adminPasswordRepository.save(new AdminPassword(admin, password));
            return admin;
        }
    }

    @Override
    public void checkAllMember(HttpSession session) {
        Iterable<Member> members = memberRepository.findAllByMemTypeEquals(0);
        session.setAttribute("members", members);
    }

    @Override
    public void checkAllOrder(HttpSession session) {
        List<Order> orders = new ArrayList<>();
        Iterable<Order> os = orderRepository.findAll();
        os.forEach(o -> orders.add(o));
        session.setAttribute("orders", orders);
    }

    @Override
    public void checkAllRestaurant(HttpSession session) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();
        List<Restaurant> rs = restaurantRepository.findAll();
        rs.forEach(r -> restaurants.add(r));
        session.setAttribute("restaurants", restaurants);
    }

    @Override
    public Integer deleteRestaurant(Integer restId) {
        Restaurant restaurant = restaurantRepository.findByRestId(restId);
        if (restaurant.getIsDeleted() == 0) {
            restaurant.setIsDeleted(1);
        } else {
            restaurant.setIsDeleted(0);
        }
        restaurantRepository.save(restaurant);
        return restaurant.getIsDeleted();
    }

    @Override
    public void deleteMember(String memEmail) {
//        memberRepository.deleteById(memEmail);
        memberPasswordRepository.deleteById(memEmail);
    }

    @Override
    public void editOrder(Long orderId, String state) {
        OrderState orderState = null;
        for (OrderState o : OrderState.values()) {
            if (o.toString().equals(state)) {
                orderState = o;
            }
        }
        Order order = orderRepository.findById(orderId).get();
        order.setOrderState(orderState);
        orderRepository.save(order);
    }
}
