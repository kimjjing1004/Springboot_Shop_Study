package com.shop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "cart")
@Data
public class Cart extends BaseEntity {

    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    // @OneToOne 어노테이션을 이용해 회원 엔티티와 일대일로 매핑을 합니다.

    @JoinColumn(name = "member_id")
    // JoinColumn 어노테이션을 이용해 매핑할 외래키를 지정합니다.
    // name 속성에는 매핑할 외래키의 이름을 설정합니다.
    // JoinColumn 의 name 을 명시하지 않으면 JPA 가 알아서 ID 를 찾지만
    // 컬럼명이 원하는 대로 생성되지 않을 수 있기 때문에 직접 지정하겠습니다.
    private Member member;

    public static Cart createCart(Member member) {
        Cart cart = new Cart();
        cart.setMember(member);
        return cart;
    }

}
