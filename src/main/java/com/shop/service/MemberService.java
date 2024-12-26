package com.shop.service;

import com.shop.entity.Member;
import com.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
// 비즈니스 로직을 담당하는 서비스 계층 클래스에 @Transactional 어노테이션을 선언합니다.
// 로직을 처리하다가 에러가 발생했다면, 변경된 데이터를 이전 상태로 콜백 시켜줍니다.

@RequiredArgsConstructor
// 빈을 주입하는 방법으로는 @Autowired 어노테이션을 이용하거나,
// 필드 주입(Setter 주입), 생성자 주입을 이용하는 방법이 있습니다.
// @RequiredArgsConstructor 어노테이션은 final 이나 @NonNull 이 붙은 필드에 생성자를 생성해줍니다.
// 빈에 생성자가 1개이고 생성자의 파라미터 타입이 빈으로 등록이 가능하다면
// @Autowired 어노테이션 없이 의존성 주입이 가능합니다.

// MemberService 가 UserDetailsService 를 구현합니다.
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    // 이미 가입된 회원의 경우 IllegalStateException 예외를 발생시킵니다.
    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    // UserDetailsService 인터페이스의 loadUserByUsername() 메소드를 오버라이딩합니다.
    // 로그인할 유저의 email 을 파라미터로 전달받습니다.
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        // UserDetail 을 구현하고 있는 User 객체를 반환해줍니다.
        // User 객체를 생성하기 위해서 생성자로 회원의 이메일, 비밀번호, role 을 파라미터 넘겨 줍니다.
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
