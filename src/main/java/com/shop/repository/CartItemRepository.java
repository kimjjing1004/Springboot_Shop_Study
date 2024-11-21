package com.shop.repository;

import com.shop.dto.CartDetailDto;
import com.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("SELECT new com.shop.dto.CartDetailDto(ci.id, i.itemNm, i.price, ci.count, im.imgUrl) "
            + "FROM CartItem ci, ItemImg im "
            + "JOIN ci.item i " + "WHERE ci.cart.id = :cartId " + "AND im.item.id = ci.item.id " + "AND im.repimgYn = 'Y' " + "ORDER BY ci.regTime DESC")
    List<CartDetailDto> findCartDetailDtoList(Long cartId);

}
