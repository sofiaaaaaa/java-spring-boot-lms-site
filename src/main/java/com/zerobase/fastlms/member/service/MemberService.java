package com.zerobase.fastlms.member.service;

import com.zerobase.fastlms.admin.dto.MemberDto;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.model.MemberInput;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
import com.zerobase.fastlms.member.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface MemberService extends UserDetailsService {

	boolean register(Member parameter);

	// UUID에 해당하는 계정을 활성화함
	boolean emailAuth(String uuid);

	// 입력한 이메일로 비밀번호 초기화 정보 전송
    boolean sendResetPassword(ResetPasswordInput parameter);

    // 입력받은 uuid에 대해 패스워드 초기화
	boolean resetPassword(String id, String password);

	// 입력받은 uuid값이 유효한지 확인
	boolean checkResetPassword(String uuid);

	// 회원 목록 조회 (관리자 페이지 전용 메뉴)
	List<MemberDto> list();
}
