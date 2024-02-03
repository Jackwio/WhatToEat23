package com.my.service;

import com.my.pojo.Admin;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

public interface AdminService {
    Admin loginAdmin(Map<String, String> adminEmailPasswordMap, HttpSession session);

    Admin registerAdmin(HttpSession session, String adminEmail, String password);

    void checkAllMember(HttpSession session);

    void checkAllOrder(HttpSession session);

    void checkAllRestaurant(HttpSession session);

    Integer deleteRestaurant(Integer restId);

    void deleteMember(String memEmail);

    void editOrder(Long orderId, String state);
}
