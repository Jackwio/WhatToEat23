package com.my.service;

import com.my.pojo.CartItem;
import com.my.pojo.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;

public interface OrderService{

    void order(HttpSession session);
    Integer calculateAll(List<CartItem> cartItems);

    void goToPay(HttpSession session, HttpServletRequest request);

    void getCommentOrder(HttpServletRequest request, HttpSession session, Member member);
}
