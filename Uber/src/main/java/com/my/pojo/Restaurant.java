package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="tbl_restaurant")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer restId;
    private String restName;
    private String restLocation;
    private Integer restPrice;
    private Integer restOpenTime;
    private Integer restCloseTime;
    private String restFoodType;
    private String restDietConstraint;
    private Double restRatings;
    private String restPhoto;
    private Integer ordersNum;
    private Integer sales;
    private Integer isDeleted;
    private String ownerEmail;

    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods;

    public Restaurant(String restName, String restLocation, Integer restPrice, Integer restOpenTime, Integer restCloseTime, String restFoodType, String restDietConstraint, Integer ordersNum) {
        this.restName = restName;
        this.restLocation = restLocation;
        this.restPrice = restPrice;
        this.restOpenTime = restOpenTime;
        this.restCloseTime = restCloseTime;
        this.restFoodType = restFoodType;
        this.restDietConstraint = restDietConstraint;
        this.ordersNum=ordersNum;
    }

    public Restaurant(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Restaurant(String restPhoto) {
        this.restPhoto = restPhoto;
    }

    public Restaurant(Integer restId, Double restRatings) {
        this.restId = restId;
        this.restRatings = restRatings;
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "restId=" + restId +
                '}';
    }
}
