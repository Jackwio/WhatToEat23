package com.my.pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.my.Enum.OrderState;
import com.my.Enum.RestOrderState;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_order")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    private Integer orderTotalPrice;
    private Integer orderQuality;
    private LocalDateTime orderDateTime;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "member")
    private Member member;
    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = "restaurant")
    private Restaurant restaurant;
    @Enumerated(EnumType.ORDINAL)
    private RestOrderState restAccepted;
    @Enumerated(EnumType.ORDINAL)
    private OrderState orderState;

//    @JsonIgnore
    @OneToMany(mappedBy = "orderId")
    private List<OrderItem> orderItems;

    public Order(RestOrderState restAccepted) {
        this.restAccepted = restAccepted;
    }

    public Order(OrderState orderState) {
        this.orderState = orderState;
    }

    public Order(Integer orderTotalPrice, Integer orderQuality, LocalDateTime orderDateTime, Member member, Restaurant restaurant, RestOrderState restAccepted, OrderState orderState) {
        this.orderTotalPrice = orderTotalPrice;
        this.orderQuality = orderQuality;
        this.orderDateTime = orderDateTime;
        this.member = member;
        this.restaurant = restaurant;
        this.restAccepted = restAccepted;
        this.orderState = orderState;
    }
}
