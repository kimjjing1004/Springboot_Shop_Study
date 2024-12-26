package com.shop.config;

import com.shop.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // SpringSecurityFilterChain 이 자동으로 포함하여 보안 설정 커스터마이징
public class SecurityConfig {

    @Autowired
    MemberService memberService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // http 요청에 대한 보안을 설정합니다.
        // 페이지 권한 설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정을 작성합니다.
        http.formLogin()
                // 로그인 페이지 URL 을 설정합니다.
                .loginPage("/members/login")

                // 로그인 성공 시 이동할 URL 을 설정합니다.
                .defaultSuccessUrl("/")

                // 로그인 시 사용할 파라미터 이름으로 email 을 지정합니다.
                .usernameParameter("email")

                // 로그인 실패 시 이동할 URL 을 설정합니다.
                .failureUrl("/members/login/error")

                .and()
                .logout()

                // 로그아웃 URL 을 설정합니다.
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))

                // 로그아웃 성공 시 이동할 URL 을 설정합니다.
                .logoutSuccessUrl("/");

        // 시큐리티 처리에 HttpServletRequest 를 이용한다는 것을 의미합니다.
        http.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()

                // permitAll() 을 통해 모든 사용자가 인증(로그인)없이 해당 경로에 접근할 수 있도록 설정합니다.
                // 메인 페이지, 회원 관련 URL, 뒤에서 만들 상품 상세 페이지, 상품 이미지를 불러오는 경로가 이에 해당합니다.
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()

                // /admin 으로 시작하는 경로는 해당 계정이 ADMIN Role 일 경우에만 접근 가능하도록 설정합니다.
                .mvcMatchers("/admin/**").hasRole("ADMIN")

                // .mvcMatchers() 로 설정해준 경로를 제외한 나머지 경로들은 모두 인증을 요구하도록 설정합니다.
                .anyRequest().authenticated();

        // 인증되지 않은 사용자가 리소스에 접근하였을 때 수행되는 핸들러를 등록합니다.
        http.exceptionHandling()
                .authenticationEntryPoint(new CustomAuthenticationEntryPoint());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 비밀번호를 DB 에 그대로 저장했을 경우, DB 가 해킹당하면 고객의 회원 정보가 노출됩니다.
        // 그래서 BCryptPasswordEncoder 의 해시 함수를 이용하여 비밀번호를 암호화하여 저장합니다.
        // BCryptPasswordEncoder 를 빈으로 등록하여 사용합니다.
        return new BCryptPasswordEncoder();
    }

}