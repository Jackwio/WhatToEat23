package com.my.controller;

import com.my.service.CartItemService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemService cartItemService;

    public CartController() {
    }

    //    加入購物車，更新內容
    @PostMapping("/food/{foodId}")
    public String addCart(@PathVariable("foodId") Integer foodId,
                          HttpSession session) {
        cartItemService.addCartItem(session, foodId);
        //跳回餐廳菜單畫面
        return "rest/menu::cartItemBlock";
    }

    //    編輯購物車數量
    @PatchMapping("/food/{foodId}/{change}")
    public String editCart(@PathVariable("foodId") Integer foodId,
                           @PathVariable("change") Integer change, HttpSession session) {
        cartItemService.editCartItem(session, foodId, change);
        return "rest/main::cartItemBlock";
    }

    //    更新購物車上數量
    @PatchMapping("/numbers")
    public String changeCartNumber(HttpSession session) {
        return "rest/main::cartNumber";
    }

    //    刪除購物車東西
    @DeleteMapping("/food/{foodId}")
    public String deleteCart(HttpSession session, @PathVariable("foodId") Integer foodId) {
        cartItemService.deleteCartItem(session, foodId);
        //跳回購物車頁面(右彈窗)
        return "rest/main::cartItemBlock";
    }
}
