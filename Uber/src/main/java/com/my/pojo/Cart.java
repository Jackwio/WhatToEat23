package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;


@Entity
@Table(name = "tbl_cart")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer cartId;

    //@JoinColumn指定資料庫字段名，雖然屬性名和字段名相同，但是和資料庫關連到的屬性名不相同
    @OneToOne
    @JoinColumn(name="member",referencedColumnName = "memEmail")
    private Member member;

    private Integer itemNum;

    @OneToMany(mappedBy = "cart",cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<CartItem> cartItems;

    @Transient  //表示該實體屬性是不需要再資料庫保存的
    public static Map<Integer, CartItem> tempCart;

    public Cart(Integer cartId, Member member) {
        this.cartId = cartId;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return Objects.equals(cartId, cart.cartId);
    }

}
