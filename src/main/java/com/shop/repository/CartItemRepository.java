package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // 카트 아이디와 상품 아이디를 이용해서 상품이 장바구니에 들어있는지 조회합니다.
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    // CartDetailDto 의 생성자를 이용하여 DTO 를 반환할 때는
    // "new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl)" 처럼
    // new 키워드와 해당 DTO 의 패키지, 클래스명을 적어줍니다.
    // 또한 생성자의 파라미터 순서는 DTO 클래스에 명시한 순으로 넣어주어야 합니다.
    @Query("SELECT new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) "
            + "FROM CartItem ci, ItemImg im "
            + "JOIN ci.item i "
            + "WHERE ci.cart.id = :cartId "

            // 장바구니에 담겨있는 상품의 대표 이미지만 가지고 오도록 조건문을 작성합니다.
            + "AND im.item.id = ci.item.id "
            + "AND im.repimgYn = 'Y' "

            + "ORDER BY ci.regTime DESC")
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}
