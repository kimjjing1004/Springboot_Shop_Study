package com.shop.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "cart_item")
public class CartItem extends BaseEntity {

    @Id
    @GeneratedValue
    @Column(name = "cart_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    // 하나의 장바구니에는 여러 개의 상품을 담을 수 있으므로
    // @ManyToOne 어노테이션을 이용하여 다대일 관계로 매핑합니다.
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    // 장바구니에 담을 상품의 정보를 알아야 하므로 상품 엔티티를 매핑해줍니다.
    // 하나의 상품은 여러 장바구니의 장바구니 상품으로 담길 수 있으므로
    // 마찬가지로 @ManyToOne 어노테이션을 이용하여 다대일 관계로 매핑합니다.
    private Item item;

    // 같은 상품을 장바구니에 몇 개 담을지 저장합니다.
    private int count;

    public static CartItem createCartItem(Cart cart, Item item, int count) {
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setItem(item);
        cartItem.setCount(count);
        return cartItem;
    }

    // 장바구니에 기존에 담겨 있는 상품인데, 해당 상품을 추가로 장바구니에 담을 때
    // 기존 수량에 현재 담을 수량을 더 해줄 때 사용할 메소드입니다.
    public void addCount(int count) {
        this.count += count;
    }

    public void updateCount(int count) {
        this.count += count;
    }

}
