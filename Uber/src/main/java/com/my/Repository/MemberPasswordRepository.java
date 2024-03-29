package com.my.Repository;


import com.my.pojo.MemPassword;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberPasswordRepository extends BaseRepository<MemPassword,String> {
    MemPassword findByMemEmail(String memEmail);
}
