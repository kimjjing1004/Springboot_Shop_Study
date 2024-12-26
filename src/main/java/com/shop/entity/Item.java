package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.exception.OutOfStockException;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name="item")
// @Entity @Table
// Item 클래스를 entity 로 선언합니다.
// 또한 @Table 어노테이션을 통해 어떤 테이블과 매핑될지를 지정합니다.
// item 테이블과 매핑되도록 name 을 item 으로 지정합니다.
@Data
public class Item extends BaseEntity {

    @Id
    @Column(name="item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    // entity 로 선언한 클래스는 반드시 기본키를 가져야 합니다.
    // 기본키가 되는 멤버변수에 @Id 어노테이션을 붙여줍니다.
    // 그리고 테이블에 매핑될 컬럼의 이름을 @Column 어노테이션을 통해 설정해줍니다.
    // Item 클래스의 id 변수와 item 테이블의 item_id 컬럼이 매핑되도록 합니다.
    // 마지막으로 @GeneratedValue 어노테이션을 통해 기본키 생성 전략을 AUTO 로 지정하겠습니다.
    private Long id; // 상품 코드

    @Column(nullable = false, length = 50)
    // @Column 어노테이션의 nullable 속성을 이용해서 항상 값이 있어야 하는 필드는 not null 설정을 합니다.
    // String 필드는 default 값으로 255 가 설정돼 있습니다.
    // 각 String 필드마다 필요한 길이를 length 속성에 default 값을 세팅 합니다.
    private String itemNm; // 상품명

    @Column(name="price", nullable = false)
    private int price; // 가격

    @Column(nullable = false)
    private int stockNumber; // 재고수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; // 상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; // 상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemNm = itemFormDto.getItemNm();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
    }

    public void removeStock(int stockNumber) {

        // 상품의 재고 수량에서 주문 후 남은 재고 수량을 구합니다.
        int restStock = this.stockNumber - stockNumber;

        if (restStock < 0) {
            // 상품의 재고가 주문 수량보다 작을 경우 재고 부족 예외를 발생시킵니다.
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }

        // 주문 후 남은 재고 수량을 상품의 현재 재고 값으로 할당합니다.
        this.stockNumber = restStock;
    }

    // 상품의 재고를 증가시키는 메소드입니다.
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }
}
