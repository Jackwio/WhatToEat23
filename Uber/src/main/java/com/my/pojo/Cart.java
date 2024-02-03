package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;

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
    @JoinColumn(name = "member")
    private Member member;

    private Integer itemNum;
    private Integer cartItemTotal;

    //orphanRemoval = true
    //如果从 cartItems 集合中移除了某个 CartItem，并且该 CartItem 没有被其他地方引用，那么在保存父实体 Cart 时，JPA 会检测到这个孤儿实体，并将其从数据库中删除。
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE},orphanRemoval = true)
    private List<CartItem> cartItems;

    @Transient  //表示該實體屬性是不需要再資料庫保存的
    public static Cart tempCart;

    public Cart(Integer cartId, Member member) {
        this.cartId = cartId;
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cart cart = (Cart) o;
        return cart.getCartId() == this.cartId;
    }

    public Cart(Member member, Integer itemNum, Integer cartItemTotal) {
        this.member = member;
        this.itemNum = itemNum;
        this.cartItemTotal = cartItemTotal;
    }
}
