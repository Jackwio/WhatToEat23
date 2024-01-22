package com.my.service;


import com.my.Enum.RestOrderState;
import com.my.Repository.*;
import com.my.pojo.*;
import com.my.utils.IdUtils;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MemberPasswordRepository memberPasswordRepository;
    @Autowired
    private RatingsRepository ratingsRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private MailService mailService;


    @Override
    public Integer registerMember(HttpSession session, String memEmail) {

        Member member = memberRepository.findByMemEmail(memEmail);
        if (member != null) {
            return 0;
        }
        String UUID = IdUtils.getUUID();
        member = new Member();
        member.setMemEmail(memEmail);
        mailService.sendEmail("註冊驗證", "你的驗證碼為 : " + UUID, memEmail);
        session.setAttribute("code", UUID);
        session.setAttribute("member", member);
        return 1;
    }

    @Override
    public Integer validCode(HttpSession session, String code) {
        String c = (String) session.getAttribute("code");
        if (c.equals(code)) {
            Member member = (Member) session.getAttribute("member");
//            memberRepository.save(member);
            session.removeAttribute("code");
            return 1;
        }
        return 0;
    }

    @Override
    public Integer validPhoneNumber(HttpSession session, String memPhoneNum) {
        Member memPhoneNumTemp = memberRepository.findByMemPhoneNum(memPhoneNum);
        if (memPhoneNumTemp != null) {
            return 0;
        }
        Member member = (Member) session.getAttribute("member");
        member.setMemType(0);
        member.setMemPhoneNum(memPhoneNum);
        String memEmail = member.getMemEmail();
        member.setMemName(memEmail.substring(0, memEmail.indexOf("@")));
        memberRepository.save(member);
        session.removeAttribute("member");
        return 1;
    }

    @Override
    public Member loginMember(String memEmail, String password, HttpSession session, HttpServletRequest request) {

        deleteAllMessage(session);
//        透過email去尋找會員
        Member tempMember = memberRepository.findByMemEmail(memEmail);
//        找不到email
        if (tempMember == null) {
            return null;
        }
//        如果找的到，繼續判斷密碼是否正確，若正確，把當前使用者保存在session裡
        MemPassword memPassword = memberPasswordRepository.findByMemEmail(memEmail);
        if (memPassword == null) {
            return null;
        }
        if (password.equals(memPassword.getPassword())) {
            if (tempMember.getMemType() == 1) {
                int orderTotalRevenues = 0;
                Restaurant restaurant = restaurantRepository.findByOwnerEmail(tempMember.getMemEmail());
                List<Order> orders = orderRepository.findAllByRestaurant_RestIdAndRestAccepted(restaurant.getRestId(), RestOrderState.FINISH);
                for (Order order : orders) {
                    orderTotalRevenues += order.getOrderTotalPrice();
                }
                restaurant.getFoods().size();
                session.setAttribute("orderAllNumbers", orders.size());
                session.setAttribute("orderAllRevenues", orderTotalRevenues);
                session.setAttribute("currentUser", tempMember);
                session.setAttribute("currentRestaurant", restaurant);
//                restaurantBackService.getOrderByState(request, session, 0);
//                List<Order> orderItems = (List<Order>) request.getAttribute("orderItems");
//                session.setAttribute("orderNumbers", orderItems.size());
//                restaurantBackService.getAverRating(restaurant.getRestId(),session);
                return tempMember;
            }
            //在涉及到懒加载的情况下，问题可能出现在事务外部访问懒加载属性的时候。这是因为 Hibernate 的默认行为是在事务结束时关闭 Session
            Cart cart = tempMember.getCart();
            cart.getCartItems().size();
            if (Cart.tempCart != null && Cart.tempCart.size() != 0) {
                processVisitorCartItem(tempMember);
            }
            //強制加載lazy屬性
            tempMember.getFavoriteRestaurants().size();

            List<Integer> favoriteRestaurantIds = tempMember.getFavoriteRestaurants().stream().map(restaurant -> restaurant.getRestId()).collect(Collectors.toList());
            session.setAttribute("currentUser", tempMember);
            session.setAttribute("favoriteRestaurantIds", favoriteRestaurantIds);
            return tempMember;
        }
//        密碼不正確，直接return，並在畫面顯示uid或密碼錯誤
        return null;
    }

    @Override
    public void processVisitorCartItem(Member member) {
        Cart cart = member.getCart();
        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem item : Cart.tempCart.values()) {
            Food food = item.getFood();
            CartItem cartItem = cartItemRepository.findByFood_FoodIdAndCart_CartId(food.getFoodId(), cart.getCartId());
            if (cartItem == null) {
                CartItem item1 = new CartItem(cart, food, item.getFoodNum());
                cartItems.add(item1);
                cartItemRepository.save(item1);
            } else {
                CartItem item1 = new CartItem(cartItem.getCartItemId(), cart, food, item.getFoodNum() + cartItem.getFoodNum());
                cartItems.stream().filter(item2 -> item2.getCart() == item1.getCart() && item2.getFood() == item1.getFood())
                        .findFirst().ifPresent(item2 -> {
                            int num = item2.getFoodNum() + item1.getFoodNum();
                            item2.setFoodNum(num);
                        });
                cartItemRepository.save(item1);
            }
        }
        Cart.tempCart.clear();
    }

    @Override
    public void collectRest(Integer restId, HttpSession session) {
        editeCollectRest(restId, session, 1);
    }


    @Override
    public void cancelCollectRest(Integer restId, HttpSession session) {
        editeCollectRest(restId, session, -1);
    }

    @Override
    public void editeCollectRest(Integer restId, HttpSession session, Integer change) {
        List<Integer> favoriteRestaurantIds;
        Member member = (Member) session.getAttribute("currentUser");
        Restaurant restaurant = restaurantRepository.findByRestId(restId);
        List<Restaurant> favoriteRestaurants = member.getFavoriteRestaurants();
        if (change == 1) {
            favoriteRestaurants.add(restaurant);
            favoriteRestaurantIds = favoriteRestaurants.stream().map(restaurant1 -> restaurant1.getRestId()).collect(Collectors.toList());
        } else {
            favoriteRestaurants = favoriteRestaurants.stream().filter(rest -> rest.getRestId() != restId).collect(Collectors.toList());
            favoriteRestaurantIds = favoriteRestaurants.stream().map(rest -> rest.getRestId()).collect(Collectors.toList());
        }
        member.setFavoriteRestaurants(favoriteRestaurants);
        memberRepository.save(member);
        session.setAttribute("favoriteRestaurantIds", favoriteRestaurantIds);
    }

    @Override
    public void goToProfile(HttpServletRequest request, HttpSession session) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        MemPassword password = memberPasswordRepository.findByMemEmail(currentUser.getMemEmail());
        session.setAttribute("password", password.getPassword());
    }

    @Transactional
    @Override
    public Integer updateEmail(HttpSession session, String memEmail) {
        Member member = memberRepository.findByMemEmail(memEmail);
        if (member != null) {
            return 0;
        }
        Member currentUser = (Member) session.getAttribute("currentUser");
        currentUser.setMemEmail(memEmail);
        memberRepository.updateMemEmailByMemName(memEmail,currentUser.getMemName());
        return 1;
    }

    @Override
    public Integer updateName(HttpSession session, String memName) {
        Member member = memberRepository.findByMemName(memName);
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (member != null && !member.getMemEmail().equals(currentUser.getMemEmail())) {
            return 0;
        }
        currentUser.setMemName(memName);
        memberRepository.save(currentUser);
        return 1;
    }

    @Override
    public Integer updatePhone(HttpSession session, String memPhoneNum) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        Member member = memberRepository.findByMemPhoneNum(memPhoneNum);
        if (member != null && !member.getMemEmail().equals(currentUser.getMemEmail())) {
            return 0;
        }
        currentUser.setMemPhoneNum(memPhoneNum);
        memberRepository.save(currentUser);
        return 1;
    }

    @Override
    public Integer editMemberInfo(Member member, String password, HttpSession session, HttpServletRequest request) {

        //檢查電話號碼是否重複
        Member tempMember = memberRepository.findByMemPhoneNum(member.getMemPhoneNum());
        Member currentUser = (Member) session.getAttribute("currentUser");
        if (tempMember != null && !member.getMemEmail().equals(tempMember.getMemEmail())) {
            return 0;
        }
        currentUser.setMemPhoneNum(member.getMemPhoneNum());
        currentUser.setMemName(member.getMemName());
        //暫時儲存舊密碼
        MemPassword tempPassword = memberPasswordRepository.findByMemEmail(member.getMemEmail());
        //如果新密碼跟舊密碼不同，更新密碼
        if (!tempPassword.equals(password) && !password.equals("")) {
            memberPasswordRepository.save(new MemPassword(member.getMemEmail(), password));
        }
        //更新資料庫資料
        memberRepository.save(currentUser);
        return 1;
    }

    @Override
    public void getMemberOrder(HttpSession session, HttpServletRequest request) {
        Member currentUser = (Member) session.getAttribute("currentUser");
        List<Order> orders = orderRepository.findAllByMember_MemEmail(currentUser.getMemEmail());
        request.setAttribute("orders", orders);
    }

    @Override
    public void goToComment(Long orderId, String comment, Integer star, HttpSession session) {
        comment(orderId, comment, star, session);
        restaurantService.updateAverageRating(session, orderId, star);
    }

    @Override
    public void cancelComment(Long orderId, HttpSession session) {
        comment(orderId, null, null, session);
    }

    @Override
    public void comment(Long orderId, String comment, Integer star, HttpSession session) {

        List<Order> currentCommentOrder = (List<Order>) session.getAttribute("currentCommentOrder");
        Member currentUser = (Member) session.getAttribute("currentUser");
        currentCommentOrder.removeIf(commentOrder -> commentOrder.getOrderId() == orderId);
        Optional<Order> o = orderRepository.findById(orderId);
        Order order = o.get();
        ratingsRepository.save(new Ratings(order, currentUser, star, comment, 1));
        if (currentCommentOrder.size() == 0) {
            session.removeAttribute("currentCommentOrder");
        }
    }

    @Override
    public void deleteAllMessage(HttpSession session) {
        //取消綁定當前使用者相關資訊
        session.removeAttribute("position");
        session.removeAttribute("time");
        session.removeAttribute("menuType");
        session.removeAttribute("menuMoney");
        session.removeAttribute("foodClass");
        session.removeAttribute("menuConstraint");
        session.removeAttribute("currentUser");
        session.removeAttribute("keyword");
        session.removeAttribute("restaurants");
        session.removeAttribute("pageNumber");
        session.removeAttribute("maxPageNumber");
        session.removeAttribute("cartItems");
    }
}
