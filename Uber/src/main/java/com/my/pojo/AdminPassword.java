package com.my.pojo;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="tbl_admin_password")
@Getter
@Setter
@IdClass(AdminPassword.class)
@NoArgsConstructor
@AllArgsConstructor
public class AdminPassword implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name="adminEmail")
    private Admin adminEmail;
    @Id
    private String password;
}
