package com.my.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.my.Interceptor.IsMemberInterceptor;
import com.my.pojo.Order;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //設置通往主畫面
        registry.addViewController("/").setViewName("user/index");
        registry.addViewController("/tst").setViewName("tst");
        registry.addViewController("/test2").setViewName("test2");
        registry.addViewController("/test3").setViewName("test3");
        //獲取使用者歷史訂單
        registry.addViewController("/lookOrders").setViewName("user/order");
        //從註冊頁面前往輸入驗證碼頁面
        registry.addViewController("/goValidCode").setViewName("user/registerValid");
        //從輸入驗證碼頁面前往輸入電話
        registry.addViewController("/goPhoneNumInput").setViewName("user/phoneNumInput");
        //個人資訊更新的三個
        registry.addViewController("/goUpdatePhone").setViewName("user/goUpdatePhone");
        registry.addViewController("/goUpdateName").setViewName("user/goUpdateName");
        registry.addViewController("/goUpdateEmail").setViewName("user/goUpdateEmail");
        registry.addViewController("/goUpdatePassword").setViewName("user/goUpdatePassword");

        registry.addViewController("/loginUser").setViewName("user/login");
        registry.addViewController("/main").setViewName("rest/main");
        registry.addViewController("/registerUser").setViewName("user/register");
        registry.addViewController("/logOut").setViewName("user/login");
        registry.addViewController("/goToTurnTable").setViewName("restaurant/turntable");
        registry.addViewController("/receiveOrder").setViewName("receiveOrder");
        registry.addViewController("/goToLookOrder").setViewName("user/order");

        registry.addViewController("/goToRegisterRest").setViewName("restaurantBack/registerRest");
        registry.addViewController("/goToRestBack").setViewName("restaurantBack/index");
        registry.addViewController("/goToRest").setViewName("restaurantBack/restaurant");
        registry.addViewController("/goToOrderManage").setViewName("restaurantBack/orderManage");
        registry.addViewController("/goToReceiveOrder").setViewName("restaurantBack/receiveOrder");

        //跳到管理員登入頁面
        registry.addViewController("/goLoginAdmin").setViewName("admin/login");
        registry.addViewController("/goToAdmin").setViewName("admin/index");
        registry.addViewController("/goToAddAdmin").setViewName("admin/addAdmin");

        registry.addViewController("/registerRest").setViewName("restBack/registerRest");

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/images/**")
                .addResourceLocations("file:"+System.getProperty("user.dir")
                        +"\\Uber\\src\\main\\resources\\webapp\\static\\images\\");
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new IsMemberInterceptor()).addPathPatterns("/goToPay");
    }
}
