package com.zerobase.fastlms.member.service.impl;

import com.zerobase.fastlms.components.MailComponents;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.exception.MemberNotEmailAuthException;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final MailComponents mailComponents;

  /** 회원가입 */
  @Override
  public boolean register(Member parameter) {

    Optional<Member> optionalMember = memberRepository.findById(parameter.getUserId());
    if (optionalMember.isPresent()) {
      return false;
    }

    String uuid = UUID.randomUUID().toString();

    String encPassword = BCrypt.hashpw(parameter.getPassword(), BCrypt.gensalt());

    //    @Builder 를 entity 클래스에 추가
    Member member =
        Member.builder()
            .userId(parameter.getUserId())
            .userName(parameter.getUserName())
            .phone(parameter.getPhone())
            .password(encPassword)
            .regDt(LocalDateTime.now())
            .emailAuthYn(false)
            .emailAuthKey(uuid)
            .build();

    memberRepository.save(member);

    String email = parameter.getUserId();
    String subject = "사이트가입축하메일";
    String text = "<h1>축하</h1>";
    text +=
        "<div><a href=\"http://localhost:8080//member/email-auth?id=" + uuid + "\">가입완료</a></div>";

    mailComponents.sendMail(email, subject, text);

    return true;
  }

  @Override
  public boolean emailAuth(String uuid) {
    Optional<Member> optionalMember = memberRepository.findByEmailAuthKey(uuid);

    if (!optionalMember.isPresent()) {
      return false;
    }

    Member member = optionalMember.get();
    member.setEmailAuthYn(true);
    member.setEmailAuthDt(LocalDateTime.now());
    memberRepository.save(member);

    return true;
  }

  @Override
  public boolean sendResetPassword(ResetPasswordInput parameter) {
    Optional<Member> optionalMember =
        memberRepository.findByUserIdAndUserName(parameter.getUserId(), parameter.getUserName());

    if (!optionalMember.isPresent()) {
      // throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
      return false;
    }

    Member member = optionalMember.get();
    String uuid = UUID.randomUUID().toString();

    member.setResetPasswordKey(uuid);
    member.setResetPasswordLimitDt(LocalDateTime.now().plusDays(1));

    memberRepository.save(member);

    String email = parameter.getUserId();
    String subject = "비밀번호초기화 메일 ";
    String text = "<h1>fastlms 비밀번호 초기화 메일 </h1>";
    text +=
        "<div><a target=\"_blank\" href=\"http://localhost:8080/member/reset/password?id="
            + uuid
            + "\">비밀번호 초기화 링크</a></div>";

    mailComponents.sendMail(email, subject, text);

    return true;
  }

  @Override
  public boolean resetPassword(String uuid, String password) {

    Optional<Member> optionalMember =
            memberRepository.findByResetPasswordKey(uuid);

    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    // 초기화 날짜 유효한지 체크
    if(member.getResetPasswordLimitDt() == null) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    String encPassword = BCrypt.hashpw(password, BCrypt.gensalt());
    member.setPassword(encPassword);
    member.setResetPasswordLimitDt(null);
    member.setResetPasswordKey("");
    memberRepository.save(member);

    return true;
  }

  @Override
  public boolean checkResetPassword(String uuid) {

    Optional<Member> optionalMember =
            memberRepository.findByResetPasswordKey(uuid);

    if (!optionalMember.isPresent()) {
      return false;
    }

    Member member = optionalMember.get();

    // 초기화 날짜 유효한지 체크
    if(member.getResetPasswordLimitDt() == null) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    if(member.getResetPasswordLimitDt().isBefore(LocalDateTime.now())) {
      throw new RuntimeException("유효한 날짜가 아닙니다.");
    }

    return true;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<Member> optionalMember = memberRepository.findById(username);

    if (!optionalMember.isPresent()) {
      throw new UsernameNotFoundException("회원 정보가 존재하지 않습니다.");
    }

    Member member = optionalMember.get();

    if (!member.isEmailAuthYn()) {
      throw new MemberNotEmailAuthException("이메일 활성화 이후에 로그인을 해주세요.");
    }

    List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
    grantedAuthorities.add(new SimpleGrantedAuthority(("ROLE_USER")));

    return new User(member.getUserId(), member.getPassword(), grantedAuthorities);
  }
}
