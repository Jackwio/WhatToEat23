package com.my.controller;


import com.my.pojo.Member;
import com.my.pojo.Restaurant;
import com.my.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class MemberController {

    @Autowired
    private MemberService memberService;

    @ResponseBody
    @RequestMapping("/login")
    public String login(String memEmail, String password, HttpSession session,HttpServletRequest request) {

        Member tempMember = memberService.loginMember(memEmail, password, session,request);
        if (tempMember == null) {
            return "帳號或密碼錯誤";
        }
        if (tempMember.getMemType() == 1) {
            return "餐廳業者登入成功";
        }
        return "會員登入成功";

    }

    @ResponseBody
    @RequestMapping("/register")
    public String register(HttpSession session,String memEmail) {
        Integer i = memberService.registerMember(session, memEmail);
        if (i == 0) {
            return "email重複";
        }
        return "";
    }

    @ResponseBody
    @RequestMapping("/validCode")
    public String validCode(HttpSession session,String code){
        int success = memberService.validCode(session,code);
        if(success==1){
            return "驗證成功";
        }
        return "驗證失敗";
    }

    @ResponseBody
    @RequestMapping("/validPhoneNum")
    public String validPhoneNum(HttpSession session,String memPhoneNum){
        int success = memberService.validPhoneNumber(session,memPhoneNum);
        if(success==1){
            return "註冊成功";
        }
        return "註冊失敗";
    }

    @RequestMapping("/goToProfile")
    public String goToProfile(HttpServletRequest request, HttpSession session) {
        memberService.goToProfile(request, session);
        return "user/profile";
    }

    @ResponseBody
    @RequestMapping("/updateName")
    public String updateName(HttpSession session,String memName){
        Integer i = memberService.updateName(session,memName);
        if(i==0){
            return "更新失敗，名稱重複";
        }
        return "更新成功";
    }

    @ResponseBody
    @RequestMapping("/updatePhone")
    public String updatePhone(HttpSession session,String memPhoneNum){
        Integer i = memberService.updatePhone(session,memPhoneNum);
        if(i==0){
            return "更新失敗，名稱重複";
        }
        return "更新成功";
    }

    @ResponseBody
    @RequestMapping("/updateEmail")
    public String updateEmail(HttpSession session,String memEmail){
        Integer i = memberService.updateEmail(session,memEmail);
        if(i==0){
            return "更新失敗，email重複";
        }
        return "更新成功";
    }

    @ResponseBody
    @RequestMapping("/editMemberInfo")
    public String editMemberInfo(Member member, String password, HttpSession session, HttpServletRequest request) {
        Integer affect = memberService.editMemberInfo(member, password, session, request);
        if (affect == 0) {
            return "電話號碼重複修改失敗";
        } else {
            return "變動成功";
        }
    }

    @RequestMapping("/getMemberOrder")
    public String getMemberOrder(HttpSession session, HttpServletRequest request) {
        memberService.getMemberOrder(session, request);
        return "forward:/goToLookOrder";
    }

    @RequestMapping("/collectRest")
    public String collectRest(Integer restId, HttpSession session, String pageNumber) {
        memberService.collectRest(restId, session);
        //判斷是在main頁面還是顯示收藏頁面
        if (pageNumber == null) {
            return "redirect:/showCollect";
        }
        return "redirect:/lookRestaurant?pageNumber=" + pageNumber;
    }

    @RequestMapping("/cancelCollectRest")
    public String cancelCollectRest(Integer restId, HttpSession session, String pageNumber) {
        memberService.cancelCollectRest(restId, session);
        //判斷是在main頁面還是顯示收藏頁面
        if (pageNumber == null) {
            return "redirect:/showCollect";
        }
        return "redirect:/lookRestaurant?pageNumber=" + pageNumber;
    }

    @RequestMapping("/showCollect")
    public String showCollect(HttpSession session) {
        return "user/collect";
    }

    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        memberService.deleteAllMessage(session);
        return "user/index";
    }

    @RequestMapping("/goToComment")
    public String goToComment(Long orderId,String comment,Integer star,HttpSession session){
        memberService.goToComment(orderId,comment,star,session);
        return "redirect:/lookRestaurant";
    }

    @RequestMapping("/cancelComment")
    public String cancelComment(Long orderId,HttpSession session){
        memberService.cancelComment(orderId,session);
        return "redirect:/lookRestaurant";
    }


}
