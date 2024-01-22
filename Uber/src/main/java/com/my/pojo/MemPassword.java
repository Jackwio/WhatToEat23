package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="tbl_mem_password")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemPassword {

    @Id
    private String memEmail;
    private String password;

    public MemPassword(String password) {
        this.password = password;
    }

}
