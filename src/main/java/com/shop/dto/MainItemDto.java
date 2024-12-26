package com.shop.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class MainItemDto {

    private Long id;

    private String itemNm;

    private String itemDetail;

    private String imgUrl;

    private Integer price;

    // 생성자에 @QueryProjection 어노테이션을 선언하여 Querydsl 로 결과 조회 시
    // MainItemDto 객체로 바로 받아 오도록 활용하겠습니다.
    @QueryProjection
    public MainItemDto(Long id, String itemNm, String itemDetail, String imgUrl, Integer price) {
        this.id = id;
        this.itemNm = itemNm;
        this.itemDetail = itemDetail;
        this.imgUrl = imgUrl;
        this.price = price;
    }
}
