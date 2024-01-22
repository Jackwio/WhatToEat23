package com.my.Repository;

import com.my.pojo.Admin;
import com.my.pojo.AdminPassword;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminPasswordRepository extends CrudRepository<AdminPassword, Admin> {
    @Query("select ap from AdminPassword ap where ap.adminEmail.adminEmail=:adminEmail ")
    AdminPassword findByAdminEmail(@Param("adminEmail") String adminEmail);
}
