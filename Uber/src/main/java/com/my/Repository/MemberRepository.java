package com.my.Repository;


import com.my.pojo.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends BaseRepository<Member, String> {

    @EntityGraph(value = "Member.All",type = EntityGraph.EntityGraphType.FETCH)
    @Override
    Optional<Member> findById(String s);

    Member findByMemPhoneNum(String memPhoneNum);
    Member findByMemName(String memName);
    List<Member> findAllByMemTypeEquals(Integer memType);
    @Procedure
    void updateMemEmailByMemName(@Param("memEmail") String memEmail, @Param("memName") String memName);
}