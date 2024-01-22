package com.my.service;

import jakarta.servlet.http.HttpSession;

public interface CartItemService {
    void addCartItem(HttpSession session, Integer foodId);

    void editCartItem(HttpSession session, Integer foodId, Integer change);

    void deleteCartItem(HttpSession session, Integer foodId);

    void changeCartItem(HttpSession session, Integer foodId, Integer change);
}
