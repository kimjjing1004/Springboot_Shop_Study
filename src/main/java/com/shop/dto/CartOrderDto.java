package com.shop.dto;

import lombok.Data;

import java.util.List;

@Data
public class CartOrderDto {

    private Long cartItemId;

    // 장바구니에는 여러 개의 상품을 주문하므로
    // CartOrderDto 클래스가 자기 자신을 List 로 가지고 있도록 만들겠습니다.
    private List<CartOrderDto> cartOrderDtoList;

}
