package com.shop.repository;

import com.shop.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

// JpaRepository 를 상속받는 ItemRepository 를 작성했습니다.
// JpaRepository 는 2개의 제네릭 타입을 사용합니다.
// 첫 번째는 entity 클래스를 넣어주고, 두 번째는 기본키 타입을 넣어줍니다.
// Item 클래스의 기본키 타입인 Long 을 넣어준다. -> JpaRepository<Item, Long>
// JpaRepository 는 기본적인 CRUD 및 페이징 처리를 위한 메소드가 정의돼 있습니다.
// entity 저장 및 수정   -> <S extends T> save(S entity)
// entity 삭제           -> void delete(T entity)
// entity 총 개수 반환   -> count()
// 모든 entity 조회      -> Iterable<T> findAll()

// QuerydslPredicateExecutor 인터페이스 상속을 추가합니다. 인터페이스 정의는 여러가지 메소드가 있습니다.
public interface ItemRepository extends JpaRepository<Item, Long>, QuerydslPredicateExecutor<Item>, ItemRepositoryCustom {

    // itemNm(상품명)으로 데이터를 조회하기 위해서 By 뒤에 필드명인 ItemNm 을 메소드의 이름에 붙여줍니다.
    // 엔티티명은 생략이 가능하므로 -> find + (엔티티 클래스명) + By + 변수이름
    // findItemByItemNm 대신에 findByItemNm 으로 메소드명을 만들어줍니다.
    // 매개 변수로는 검색할 때 사용할 상품명 변수를 넘겨줍니다.
    List<Item> findByItemNm(String itemNm);

    // 상품을 상품명과 상품 상세 설명을 OR 조건을 이용하여 조회하는 쿼리 메소드입니다.
    List<Item> findByItemNmOrItemDetail(String itemNm, String itemDetail);

    // 파라미터로 넘어온 price 변수보다 값이 작은 상품 데이터를 조회하는 쿼리 메소드입니다.
    List<Item> findByPriceLessThan(Integer price);

    List<Item> findByPriceLessThanOrderByPriceDesc(Integer price);

    // @Query 어노테이션 안에 JPQL 로 작성한 쿼리문을 넣어줍니다.
    // FROM 뒤에는 엔티티 클래스로 작성한 Item 을 지정해주었고,
    // Item 으로부터 데이터를 SELECT 하겠다는 것을 의미합니다.
    @Query("SELECT i FROM Item i WHERE i.itemDetail LIKE %:itemDetail% ORDER BY i.price DESC")
    List<Item> findByItemDetail(@Param("itemDetail") String itemDetail);
    // 파라미터에 @Param 어노테이션을 이용하여 파라미터로 넘어온 값을 JPQL 에 들어갈 변수로 지정해줄 수 있습니다.
    // 현재는 itemDetail 변수를 "LIKE % %" 사이에 ":itemDetail" 로 값이 들어가도록 작성했습니다.

    // 기존의 테이터베이스에서 사용하던 쿼리를 그대로 사용해야 할 때는
    // @Query 의 nativeQuery 속성을 사용하면 기존 쿼리를 그대로 활용할 수 있습니다.
    // value 안에 네이티브 쿼리문을 작성하고 "nativeQuery = true" 를 지정합니다.
    @Query(value = "SELECT * FROM item i WHERE i.item_detail LIKE CONCAT('%', :itemDetail, '%') ORDER BY i.price DESC", nativeQuery = true)
    List<Item> findByItemDetailByNative(@Param("itemDetail") String itemDetail);

}
