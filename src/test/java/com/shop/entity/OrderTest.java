package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@Slf4j
public class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public Item createItem() {
        Item item = new Item();
        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("상세설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());
        return item;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest() {

        Order order = new Order();

        for (int i = 0; i  < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);

            // 아직 영속성 컨텍스트에 저장되지 않은
            // orderItem 엔티티를 order 엔티티에 담아줍니다.
            order.getOrderItems().add(orderItem);
        }

        // order 엔티티를 저장하면서 강제로 flush 를 호출하여
        // 영속성 컨텍스트에 있는 객체들을 데이터베이스에 반영합니다.
        orderRepository.saveAndFlush(order);

        // 영속성 컨텍스트의 상태를 초기화합니다.
        em.clear();

        // 영속성 컨텍스트를 초기화했기 때문에 데이터베이스에서 주문 엔티티를 조회합니다.
        // SELECT 쿼리문이 실행되는 것을 콘솔창에서 확인할 수 있습니다.
        Order savedOrder = orderRepository.findById(order.getId()).orElseThrow(EntityNotFoundException::new);

        // itemOrder 엔티티 3개가 실제로 데이터베이스에 저장되었는지 검사합니다.
        assertEquals(3, savedOrder.getOrderItems().size());
    }

    // 주문 데이터를 생성해서 저장하는 메소드를 만듭니다.
    public Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = this.createOrder();

        // order 엔티티에서 관리하고 있는 orderItem 리스트의 0번째 인덱스 요소를 제거합니다.
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        // 기존에 만들었던 주문 생성 메소드를 이용하여 주문 데이터를 저장합니다.
        Order order = this.createOrder();

        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        // 영속성 컨텍스트의 상태 초기화 후 order 엔티티에 저장했던 주문 상품 아이디를 이용하여
        // orderItem 을 데이터베이스에서 다시 조회합니다.
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(EntityNotFoundException::new);

        // orderItem 엔티티에 있는 order 객체의 클래스를 출력합니다. Order 클래스가 출력되는 것을 확인할 수 있습니다.
        // 출력 결과: Order class : class com.shop.entity.Order
        log.info("Order class : " + orderItem.getOrder().getClass());
        log.info("========================================");
        orderItem.getOrder().getOrderDate();
        log.info("========================================");
    }
}
