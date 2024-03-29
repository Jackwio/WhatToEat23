package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tbl_cart_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartItemId;

    @ManyToOne
    @JoinColumn(name = "cart")
    private Cart cart;

    @OneToOne
    @JoinColumn(name = "food")
    private Food food;

    private Integer foodNum;

    public CartItem(Integer foodNum) {
        this.foodNum = foodNum;
    }

    public CartItem(Food food, Integer foodNum) {
        this.food = food;
        this.foodNum = foodNum;
    }

    public CartItem(Cart cart, Food food, Integer foodNum) {
        this.cart = cart;
        this.food = food;
        this.foodNum = foodNum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CartItem cartItem = (CartItem) o;
        return cartItem.getFood().equals(this.food);
    }

}
