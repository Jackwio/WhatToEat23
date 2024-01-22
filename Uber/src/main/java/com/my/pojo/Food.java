package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "tbl_food")
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer foodId;
    private String foodName;
    private Integer foodPrice;
    private String foodPhoto;
    private String foodType;

    @ManyToOne
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;


    public Food(Integer menuFoodId) {
        this.foodId = menuFoodId;
    }

    public Food(String foodName, Integer foodPrice, String foodPhoto, String foodType, Restaurant restaurant) {
        this.foodName = foodName;
        this.foodPrice = foodPrice;
        this.foodPhoto = foodPhoto;
        this.foodType = foodType;
        this.restaurant = restaurant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Objects.equals(foodId, food.foodId);
    }

}
