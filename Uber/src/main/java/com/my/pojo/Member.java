package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "tbl_member")
@NoArgsConstructor
@AllArgsConstructor
@NamedEntityGraph(name="Member.All",attributeNodes = {@NamedAttributeNode("favoriteRestaurants"),
        @NamedAttributeNode("orders"),@NamedAttributeNode("ratingsList")})
public class Member implements Serializable {

    public static final long serialVersionUID = 42L;

    @Id
    private String memEmail;
    private String memName;
    private String memPhoneNum;
    private Integer memType;
    private String memPhoto;

    //使用 mappedBy 指定的属性应该是 Member 实体中表示关联的属性，而不是数据库表中的列名
    //orphanRemoval = true， If you remove an entity from the association,
    // and it's no longer referenced by any other entities or collections,
    // it will be automatically deleted from the database.
    @OneToOne(mappedBy = "member", cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private Cart cart;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "tbl_favorite",
            joinColumns = {@JoinColumn(name = "member")},
            inverseJoinColumns = {@JoinColumn(name = "restaurant")}
    )
    private List<Restaurant> favoriteRestaurants;
    @OneToMany(mappedBy = "member", cascade = {CascadeType.PERSIST})
    @OrderBy("orderDateTime desc")
    private List<Order> orders;
    @OneToMany(mappedBy = "member",cascade = {CascadeType.PERSIST})
    private List<Ratings> ratingsList;

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
