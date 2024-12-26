package com.shop.entity;

import com.shop.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
@Slf4j
public class MemberTest {

    @Autowired
    MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Test
    @DisplayName("Auditing 테스트")
    @WithMockUser(username = "gildong", roles = "USER")
    // 스프링 시큐리티에서 제공하는 어노테이션으로
    // @WithMockUser 에 지정한 사용자가 로그인한 상태라고
    // 가정하고 테스트를 진행할 수 있습니다.
    public void auditingTest() {
        Member newMember = new Member();
        memberRepository.save(newMember);

        em.flush();
        em.clear();

        Member member = memberRepository.findById(newMember.getId()).orElseThrow(EntityNotFoundException::new);

        log.info("register time : " + member.getRegTime());
        log.info("update time : " + member.getUpdateTime());
        log.info("create member : " + member.getCreatedBy());
        log.info("modify member : " + member.getModifiedBy());
    }
}
