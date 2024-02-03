package com.my.Repository;


import com.my.pojo.MemPassword;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPasswordRepository extends CrudRepository<MemPassword,String> {
    MemPassword findByMemEmail(String memEmail);
    @Modifying
    @Query("update MemPassword mp set mp.memEmail=:memEmail where mp.password=:password")
    void updateMemEmailByMemName(@Param("memEmail") String memEmail, @Param("password") String password);
}
