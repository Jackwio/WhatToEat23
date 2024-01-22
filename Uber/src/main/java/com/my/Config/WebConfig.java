package com.my.Config;

import com.my.Interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        //設置通往主畫面
        registry.addViewController("/").setViewName("user/index");
        //從註冊頁面前往輸入驗證碼頁面
        registry.addViewController("/goValidCode").setViewName("user/registerValid");
        //從輸入驗證碼頁面前往輸入電話
        registry.addViewController("/goPhoneNumInput").setViewName("user/phoneNumInput");

        //個人資訊更新的三個
        registry.addViewController("/goUpdatePhone").setViewName("user/goUpdatePhone");
        registry.addViewController("/goUpdateName").setViewName("user/goUpdateName");
        registry.addViewController("/goUpdateEmail").setViewName("user/goUpdateEmail");


        registry.addViewController("/registerOption").setViewName("registerOption");
        registry.addViewController("/loginOption").setViewName("loginOption");
        registry.addViewController("/loginUser").setViewName("user/login");
        registry.addViewController("/main").setViewName("restaurant/main");
        registry.addViewController("/registerUser").setViewName("user/register");
        registry.addViewController("/logOut").setViewName("user/login");
        registry.addViewController("/editProfileUser").setViewName("editProfile");
        registry.addViewController("/notMember").setViewName("notMember");
        registry.addViewController("/turntable").setViewName("turntable");
        registry.addViewController("/receiveOrder").setViewName("receiveOrder");
        registry.addViewController("/goToLookOrder").setViewName("user/order");

        registry.addViewController("/goToRegisterRest").setViewName("restaurantBack/registerRest");
        registry.addViewController("/goToRestBack").setViewName("restaurantBack/index");
        registry.addViewController("/goToRest").setViewName("restaurantBack/restaurant");
//        registry.addViewController("/goToMenuOfRest").setViewName("restBack/menu");
        registry.addViewController("/goToOrderManage").setViewName("restaurantBack/orderManage");
        registry.addViewController("/goToReceiveOrder").setViewName("restaurantBack/receiveOrder");

        //跳到管理員登入頁面
        registry.addViewController("/goLoginAdmin").setViewName("admin/login");
        registry.addViewController("/goToAdmin").setViewName("admin/index");
        registry.addViewController("/goToAddAdmin").setViewName("admin/addAdmin");

        registry.addViewController("/registerRest").setViewName("restBack/registerRest");

        registry.addViewController("/gotest").setViewName("test");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/images/**")
                .addResourceLocations("file:"+System.getProperty("user.dir")
                        +"\\Uber\\src\\main\\resources\\webapp\\static\\images\\");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/goToPay","/logOut","/collectRest","/cancelCollectRest");
    }

//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat, SerializerFeature.DisableCircularReferenceDetect);
//        fastJsonHttpMessageConverter.setFastJsonConfig(fastJsonConfig);
//
//        List<MediaType> fastJsonMediaTypes = new ArrayList<>();
//        fastJsonMediaTypes.add(MediaType.APPLICATION_JSON);
//        fastJsonHttpMessageConverter.setSupportedMediaTypes(fastJsonMediaTypes);
//        converters.add(fastJsonHttpMessageConverter);
//    }
}
