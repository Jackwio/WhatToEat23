package com.my.pojo;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@IdClass(Ratings.class)
@Table(name = "tbl_ratings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ratings implements Serializable {

    @Id
    @OneToOne
    @JoinColumn(name="order_id")
    private Order orderId;
    @Id
    @ManyToOne
    @JoinColumn(name="member")
    private Member member;
    private Integer ratingsStar;
    private String ratingsContent;
    private Integer isComment;

}
