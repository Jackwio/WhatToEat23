package com.my.controller;


import com.my.pojo.Member;
import com.my.pojo.Ratings;
import com.my.pojo.Restaurant;
import com.my.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/member")
@SessionAttributes("password")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ResponseBody
    @PostMapping("/")
    public ResponseEntity<Map<String, String>> login(HttpSession session, Model model,
                                                     @RequestParam Map<String, String> emailPasswordMap) {
        HashMap<String, String> map = new HashMap<>();
        Member tempMember = null;
        try {
            tempMember = memberService.loginMember(session, model, emailPasswordMap);
            if (tempMember.getMemType() == 1) {
                map.put("message", "餐廳業者登入成功");
            } else {
                map.put("message", "會員登入成功");
            }
        } catch (Exception e) {
            map.put("message", "帳號或密碼錯誤");
        }
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PostMapping("/email/{memEmail}")
    public ResponseEntity<Map<String, String>> register(HttpSession session,
                                                        @PathVariable("memEmail") String memEmail) {
        HashMap<String, String> map = new HashMap<>();
        Integer i = memberService.registerMember(session, memEmail);
        if (i == 0) {
            map.put("message", "email重複");
        } else {
            map.put("message", "");
        }
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PostMapping("/VerificationCode/{code}")
    public ResponseEntity<Map<String, String>> validCode(HttpSession session,
                                                         @PathVariable("code") String code) {
        HashMap<String, String> map = new HashMap<>();
        int success = memberService.validCode(session, code);
        if (success == 1) {
            map.put("message", "驗證成功");
        } else {
            map.put("message", "驗證失敗");
        }
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PostMapping("/phoneNum/{memPhoneNum}")
    public ResponseEntity<Map<String, String>> validPhoneNum(HttpSession session,
                                                             @PathVariable("memPhoneNum") String memPhoneNum) {
        HashMap<String, String> map = new HashMap<>();
        int success = memberService.validPhoneNumber(session, memPhoneNum);
        if (success == 1) {
            map.put("message", "註冊成功");
        } else {
            map.put("message", "註冊失敗");
        }
        return ResponseEntity.ok(map);
    }

    @GetMapping("/")
    public String goToProfile(@SessionAttribute String password) {
        return "user/profile";
    }

    @ResponseBody
    @PatchMapping("/name/{memName}")
    public ResponseEntity<Map<String, String>> updateName(HttpSession session, @PathVariable("memName") String memName) {
        HashMap<String, String> map = new HashMap<>();
        try {
            memberService.updateName(session, memName);
            map.put("message", "更新成功");
        } catch (Exception e) {
            map.put("message", "更新失敗，名稱重複");
        }
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PatchMapping("/phone/{memPhoneNum}")
    public ResponseEntity<Map<String, String>> updatePhone(HttpSession session,
                                                           @PathVariable("memPhoneNum") String memPhoneNum) {
        HashMap<String, String> map = new HashMap<>();
        try {
            memberService.updatePhone(session, memPhoneNum);
            map.put("message", "更新成功");
        } catch (Exception e) {
            map.put("message", "更新失敗，名稱重複");
        }
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PatchMapping("/email/{memEmail}")
    public ResponseEntity<Map<String, String>> updateEmail(HttpSession session,
                                                           @SessionAttribute String password,
                                                           @PathVariable("memEmail") String memEmail) {
        HashMap<String, String> map = new HashMap<>();
        try {
            memberService.updateEmail(session, password, memEmail);
            map.put("message", "更新成功");
        } catch (Exception e) {
            map.put("message", "更新失敗，email重複");
        }
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PatchMapping("/password/{password}")
    public ResponseEntity<Map<String, String>> updatePassword(HttpSession session, Model model,
                                                              @PathVariable("password") String password) {
        HashMap<String, String> map = new HashMap<>();
        memberService.updatePassword(session, model, password);
        map.put("message", "更新成功");
        return ResponseEntity.ok(map);
    }

    @ResponseBody
    @PatchMapping("/photo")
    public ResponseEntity<Map<String, String>> updatePhoto(HttpSession session,
                                                           MultipartFile memPhoto) {
        HashMap<String, String> map = new HashMap<>();
        memberService.updatePhoto(session, memPhoto);
        map.put("message", "更新成功");
        return ResponseEntity.ok(map);
    }

    @GetMapping("/orders")
    public String getMemberOrder(HttpSession session, HttpServletRequest request) {
        memberService.getMemberOrder(session, request);
        return "user/order";
    }

    @PostMapping("/restaurant/{restId}/{pageNumber}")
    public String collectRest(@PathVariable("restId") Integer restId
            , HttpSession session, @PathVariable("pageNumber") Integer pageNumber) {
        memberService.collectRest(restId, session);
        return "redirect:/lookRestaurant?pageNumber=" + pageNumber;
    }

    @DeleteMapping("/restaurant/{restId}/{pageNumber}")
    public String cancelCollectRest(@PathVariable("restId") Integer restId
            , HttpSession session, @PathVariable("pageNumber") Integer pageNumber) {
        memberService.cancelCollectRest(restId, session);
        //判斷是在main頁面還是顯示收藏頁面
        if (pageNumber == 0) {
            return "redirect:/restaurants";
        }
        return "redirect:/lookRestaurant?pageNumber=" + pageNumber;
    }

    @GetMapping("/restaurants")
    public String showCollect(HttpSession session) {
        return "user/collect";
    }

    @GetMapping("/logOut")
    public String logOut(HttpSession session, SessionStatus sessionStatus) {
        memberService.deleteAllMessage(session, sessionStatus);
        return "user/index";
    }

    @PutMapping("/comment")
    public String goToComment(Ratings ratings, HttpSession session) {
        memberService.goToComment(ratings, session);
        return "redirect:/lookRestaurant";
    }

    @GetMapping("/comment")
    public String cancelComment(Ratings ratings, HttpSession session) {
        memberService.cancelComment(ratings, session);
        return "redirect:/lookRestaurant";
    }


}
