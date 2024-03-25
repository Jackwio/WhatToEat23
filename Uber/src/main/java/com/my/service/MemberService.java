package com.my.service;

import com.my.pojo.Member;
import com.my.pojo.Ratings;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public interface MemberService {

    Integer registerMember(HttpSession session, String memEmail);

    Member loginMember(HttpSession session, Model model,
                       Map<String, String> emailPasswordMap);

    //對喜歡餐廳編輯

    void collectRest(Integer restId, HttpSession session);

    void cancelCollectRest(Integer restId, HttpSession session);

    void editeCollectRest(Integer restId, HttpSession session, Integer change);

    //登出時移除使用者資訊
    void deleteAllMessage(HttpSession session, SessionStatus sessionStatus);

    void comment(Ratings ratings, HttpSession session);

    void goToComment(Ratings ratings, HttpSession session);

    void cancelComment(Ratings ratings, HttpSession session);

    //再轉為會員或登入時處理訪客資訊
    void processVisitorCartItem(Member member);

    Integer validCode(HttpSession session, String code);

    Integer validPhoneNumber(HttpSession session, String memPhoneNum,String memName,String password);

    void updateEmail(HttpSession session, String password, String memEmail);

    void updateName(HttpSession session, String memName);

    void updatePhone(HttpSession session, String memPhoneNum);

    void updatePassword(HttpSession session, Model model, String password);

    void updatePhoto(HttpSession session, MultipartFile memPhoto);
}
