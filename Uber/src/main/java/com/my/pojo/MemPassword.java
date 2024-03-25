package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name="tbl_mem_password")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemPassword implements Serializable {

    @Id
    private String memEmail;
    private String password;

    public MemPassword(String password) {
        this.password = password;
    }

}
