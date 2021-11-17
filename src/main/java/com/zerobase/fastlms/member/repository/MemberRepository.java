package com.zerobase.fastlms.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zerobase.fastlms.member.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>{

    Optional<Member> findByEmailAuthKey(String emailAuthKey);

    Optional<Member> findByUserIdAndUserName(String userId, String userName);

    Optional<Member> findByResetPasswordKey(String resetPasswordKey);

}
