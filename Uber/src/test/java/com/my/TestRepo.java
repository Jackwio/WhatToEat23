package com.my;

import com.my.pojo.Member;
import com.my.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepo {


    @Autowired
    private MemberService memberService;

    @Test
    public void testEMail(){
        Member member = new Member();
        member.setMemEmail("learningjavaweb@gmail.com");
//        memberService.registerMember(member,"123",null);
    }

}
