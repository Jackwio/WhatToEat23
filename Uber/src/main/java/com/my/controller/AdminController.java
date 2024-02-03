package com.my.controller;

import com.my.pojo.Admin;
import com.my.service.AdminService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AdminController {

    @Autowired
    private AdminService adminService;

    public AdminController() {
    }

    @ResponseBody
    @GetMapping("/admin")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestParam Map<String, String> adminEmailPasswordMap, HttpSession session) {
        HashMap<String, String> map = new HashMap<>();
        try {
            Admin admin = adminService.loginAdmin(adminEmailPasswordMap, session);
            map.put("message", admin.getAdminEmail() + "管理員登入成功");
        } catch (Exception e) {
            map.put("message", "管理員登入失敗");
        } finally {
            return ResponseEntity.ok(map);
        }
    }

    @ResponseBody
    @GetMapping("/admin/{adminEmail}/{password}")
    public ResponseEntity<Map<String, String>> adminRegister(@PathVariable("adminEmail") String adminEmail
            , HttpSession session, @PathVariable("password") String password) {
        Admin tempAdmin = adminService.registerAdmin(session, adminEmail, password);
        HashMap<String, String> map = new HashMap<>();
        //若唯一，則儲存，並回到index頁面
        if (tempAdmin != null) {
            map.put("message", tempAdmin.getAdminEmail() + "管理員添加成功");
        } else {
            map.put("message", "管理員添加失敗");
        }
        return ResponseEntity.ok(map);
    }

    @RequestMapping("/logoutAdmin")
    public String logoutAdmin(HttpSession session) {
        session.removeAttribute("admin");
        return "user/index";
    }

    @GetMapping("/members")
    public String checkAllMember(HttpSession session) {
        adminService.checkAllMember(session);
        return "admin/memberList";
    }

    @GetMapping("/orders")
    public String checkAllOrder(HttpSession session) {
        adminService.checkAllOrder(session);
        return "admin/historyOrder";
    }

    @GetMapping("/restaurants")
    public String checkAllRestaurant(HttpSession session) {
        adminService.checkAllRestaurant(session);
        return "admin/restaurantList";
    }

    @ResponseBody
    @GetMapping("/restaurants/{restId}")
    public ResponseEntity<HashMap<String, String>> stopRest(@PathVariable("restId") Integer restId) {
        Integer affect = adminService.deleteRestaurant(restId);
        HashMap<String, String> map = new HashMap<>();
        if (affect == 0) {
            map.put("message", "解鎖成功");
        } else {
            map.put("message", "封鎖成功");
        }
        return ResponseEntity.ok(map);
    }

    @RequestMapping("/deleteMember")
    public String deleteMember(String memEmail) {
        adminService.deleteMember(memEmail);
        return "redirect:/checkAllMember";
    }

    @RequestMapping("/editOrder")
    public String editOrder(Long orderId, String state) {
        adminService.editOrder(orderId, state);
        return "redirect:/checkAllOrder";
    }

}
