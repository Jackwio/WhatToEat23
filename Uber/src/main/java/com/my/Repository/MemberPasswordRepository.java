package com.my.Repository;


import com.my.pojo.MemPassword;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPasswordRepository extends CrudRepository<MemPassword,String> {
    MemPassword findByMemEmail(String memEmail);
}
