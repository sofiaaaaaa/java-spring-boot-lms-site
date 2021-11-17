package com.zerobase.fastlms.main.controller;

import com.zerobase.fastlms.components.MailComponents;
import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
@Controller
public class MainController {
	
	private final MailComponents mailComponents;
	

	@RequestMapping("/")
	public String index() {
//		String email = "jihooyim@gmail.com";
//		String subject = "aaaccc";
//		String text  = "<h1> 안녕하에숑 </h1> ";
//		mailComponents.sendMail(email, subject, text);
		return "index";
	}

}
