package com.my.Repository;


import com.my.pojo.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends CrudRepository<Member,String> {
    Member findByMemEmail(String memEmail);
    Member findByMemPhoneNum(String memPhoneNum);
    Member findByMemName(String memName);

    List<Member> findAllByMemTypeEquals(Integer memType);
    @Modifying
    @Query("update Member m set m.memEmail=:memEmail where m.memName=:memName")
    void updateMemEmailByMemName(@Param("memEmail") String memEmail, @Param("memName") String memName);
}