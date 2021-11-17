package com.zerobase.fastlms.member.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Data
public class Member {

	@Id
	private String userId;
	private String userName;
	private String phone;
	private String password;
	private LocalDateTime regDt;

	private boolean emailAuthYn;
	private LocalDateTime emailAuthDt;
	private String emailAuthKey;

	private String resetPasswordKey;
	private LocalDateTime resetPasswordLimitDt;

	private boolean adminYn;

}
