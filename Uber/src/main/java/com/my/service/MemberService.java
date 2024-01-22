package com.my.service;

import com.my.pojo.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface MemberService{

    Integer registerMember( HttpSession session,String memEmail);

    Member loginMember(String memEmail, String password, HttpSession session, HttpServletRequest request);

    //對喜歡餐廳編輯

    void collectRest(Integer restId, HttpSession session);
    void cancelCollectRest(Integer restId, HttpSession session);
    void editeCollectRest(Integer restId, HttpSession session,Integer change);

    //編輯和查看個人資訊
    void goToProfile(HttpServletRequest request, HttpSession session);

    Integer editMemberInfo(Member member, String nPassword, HttpSession session,HttpServletRequest request);

    //查看個人歷史訂單
    void getMemberOrder(HttpSession session, HttpServletRequest request);

    //登出時移除使用者資訊
    void deleteAllMessage(HttpSession session);

    void comment(Long orderId,String comment,Integer star,HttpSession session);

    void goToComment(Long orderId,String comment,Integer star,HttpSession session);

    void cancelComment(Long orderId, HttpSession session);

    //再轉為會員或登入時處理訪客資訊
    void processVisitorCartItem(Member member);
    Integer validCode(HttpSession session,String code);
    Integer validPhoneNumber(HttpSession session,String memPhoneNum);

    Integer updateEmail(HttpSession session, String memEmail);

    Integer updateName(HttpSession session, String memName);

    Integer updatePhone(HttpSession session, String memPhoneNum);
}
