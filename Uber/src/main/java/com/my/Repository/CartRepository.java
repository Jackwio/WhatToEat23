package com.my.Repository;

import com.my.pojo.Cart;
import com.my.pojo.Member;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends CrudRepository<Cart,Integer> {

    Cart findByMember_MemEmail(String memEmail);
}
