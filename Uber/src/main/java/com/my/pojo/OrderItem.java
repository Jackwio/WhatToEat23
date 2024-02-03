package com.my.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tbl_order_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemId;

//    注意order為mysql保留字，所以才改為orderId
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Order orderId;
    @OneToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "food")
    private Food food;
    private Integer orderFoodNum;
    private Integer orderFoodPrice;

    public OrderItem(Order orderId, Food food, Integer orderFoodNum, Integer orderFoodPrice) {
        this.orderId = orderId;
        this.food = food;
        this.orderFoodNum = orderFoodNum;
        this.orderFoodPrice = orderFoodPrice;
    }
}
