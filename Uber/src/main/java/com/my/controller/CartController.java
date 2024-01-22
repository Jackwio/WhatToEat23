package com.my.controller;

import com.my.service.CartItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    public CartController() {
    }

    //    加入購物車，更新內容
    @RequestMapping("/addCart")
    public String addCart(Integer foodId, HttpSession session) {
        cartItemService.addCartItem(session, foodId);
        //跳回餐廳菜單畫面
        return "restaurant/menu::cartItemBlock";
    }

    //    編輯購物車數量
    @RequestMapping("/editCart")
    public String editCart(Integer foodId, Integer change, HttpSession session) {
        cartItemService.editCartItem(session, foodId, change);
        return "restaurant/main::cartItemBlock";
    }

    //    更新購物車上數量
    @RequestMapping("/changeCartNumber")
    public String changeCartNumber(HttpSession session) {
        return "restaurant/main::cartNumber";
    }

    //    刪除購物車東西
    @RequestMapping("/deleteCart")
    public String deleteCart(HttpSession session, Integer foodId) {
        cartItemService.deleteCartItem(session, foodId);
        //跳回購物車頁面(右彈窗)
        return "restaurant/main::cartItemBlock";
    }
}
