package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="tbl_member")
@NoArgsConstructor
@AllArgsConstructor
public class Member implements Serializable {

    public static final long serialVersionUID = 42L;

    @Id
    private String memEmail;
    private String memName;
    private String memPhoneNum;
    private Integer memType;
    private String memPhoto;

    //mappedBy指定關連到的實體類的屬性
    @OneToOne(
            mappedBy = "member",
            orphanRemoval = true
    )
    private Cart cart;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="tbl_favorite",
            joinColumns = {@JoinColumn(name = "member")},
            inverseJoinColumns = {@JoinColumn(name="restaurant")}
    )
    private List<Restaurant> favoriteRestaurants;

    @Transient
    public static Member tempUser;

    public Member(String memEmail, String memName, String memPhoneNum) {
        this.memEmail = memEmail;
        this.memName = memName;
        this.memPhoneNum = memPhoneNum;
    }

    public Member(String memName, String memPhoneNum) {
        this.memName = memName;
        this.memPhoneNum = memPhoneNum;
    }

    public Member(String memEmail) {
        this.memEmail = memEmail;
    }

    public Member(String memEmail, Integer memType) {
        this.memEmail = memEmail;
        this.memType = memType;
    }
}
