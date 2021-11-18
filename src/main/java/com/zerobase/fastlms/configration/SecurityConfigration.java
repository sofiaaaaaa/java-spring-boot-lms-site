package com.zerobase.fastlms.configration;

import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfigration extends WebSecurityConfigurerAdapter {

  private final MemberService memberService;

  @Bean
  PasswordEncoder getPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  UserAuthenticationFailureHandler getFailureHandler() {
    return new UserAuthenticationFailureHandler();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.headers().frameOptions().sameOrigin();

    // antMatchers에 추가하면 로그인 페이지가 뜨지 않는다.
    http.authorizeRequests()
        .antMatchers(
            "/",
            "/member/register",
            "/member/email-auth",
            "/member/find/password",
            "/member/reset/password")
        .permitAll();

    // Admin 페이지 접근 권한
    http.authorizeRequests()
            .antMatchers("/admin/**")
            .hasAnyAuthority("ROLE_ADMIN");

    // 로그인페이지 설정
    http.formLogin().loginPage("/member/login").failureHandler(getFailureHandler()).permitAll();

    // 로그아웃
    http.logout()
        .logoutRequestMatcher(new AntPathRequestMatcher("/member/logout"))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true);


    // 에러 페이지
    http.exceptionHandling()
            .accessDeniedPage("/error/denied");

    super.configure(http);
  }

  @Override
  public void configure(AuthenticationManagerBuilder auth) throws Exception {

    // 패스워드 지정
    auth.userDetailsService(memberService).passwordEncoder(getPasswordEncoder());

    super.configure(auth);
  }
}
