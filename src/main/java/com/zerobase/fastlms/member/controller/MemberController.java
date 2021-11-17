package com.zerobase.fastlms.member.controller;

import com.zerobase.fastlms.member.entity.Member;
import com.zerobase.fastlms.member.model.ResetPasswordInput;
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
public class MemberController {

  private final MemberService memberservice;

  @RequestMapping("/member/login")
  public String login() {
    return "member/login";
  }

  @GetMapping("/member/find/password")
  public String findPassword() {
    return "member/find_password";
  }

  @PostMapping("/member/find/password")
  public String findPasswordSubmit(Model model, ResetPasswordInput parameter) {

    boolean result = memberservice.sendResetPassword(parameter);

    // 		return "redirect:/";
    model.addAttribute("result", result);
    return "member/find_password_result";
  }

  @GetMapping("/member/register")
  public String register() {
    return "member/register";
  }

  @PostMapping("/member/register")
  public String registerSubmit(Model model, HttpServletRequest request, Member parameter) {

    boolean result = memberservice.register(parameter);

    model.addAttribute("result", result);
    return "member/register_complate";
  }

  @GetMapping("/member/email-auth")
  public String emailAuth(Model model, HttpServletRequest request) {
    String uuid = request.getParameter("id");
    System.out.println(uuid);

    boolean result = memberservice.emailAuth(uuid);

    model.addAttribute("result", result);

    return "member/email_auth";
  }

  @GetMapping("/member/info")
  public String memberInfo() {
    return "member/member_info";
  }

  @GetMapping("/member/reset/password")
  public String memberResetPassword(Model model, HttpServletRequest request) {
    String uuid = request.getParameter("id");
    model.addAttribute("uuid", uuid);
    boolean result = memberservice.checkResetPassword(uuid);
    model.addAttribute("result", result);
    return "member/reset_password";
  }

  @PostMapping("/member/reset/password")
  public String memberResetPasswordSubmit(
      Model model, ResetPasswordInput parameter, HttpServletRequest request) {
    boolean result = false;
    try {
      result = memberservice.resetPassword(parameter.getId(), parameter.getPassword());
    } catch (Exception e) {

    }
    model.addAttribute("result", result);
    return "member/reset_password_result";
  }
  //		String userId = request.getParameter("userId");
  //	@RequestMapping(value="/member/register", method = RequestMethod.GET)
  // https://www.baeldung.com/spring-request-param
  //	@RequestMapping(value="/member/register", method = RequestMethod.POST)
}
