package com.zerobase.fastlms.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class MainPageController {
	
//	// thymeleaf
//	@RequestMapping("/")
//	public String index() {
//		return "index";
//	}
//

    // 인터넷 주소와 물리적일 파일을 매핑하기 위한 클래스
    @RestController
    public static class MainPageControllerTest {

    //	@GetMapping("/")
    //	public String index() {
    //		return "Hello";
    //	}
    //
    //	@RequestMapping("/hello")
    //	public String hello() {
    //		return "HelloHelloHelloHello";
    //	}
    //	@RequestMapping("/")
    //	public String index2() {
    //		return "Hello";
    //	}


    //	@RequestMapping("/hello")
    //	public void hello(HttpServletRequest request, HttpServletResponse response) throws IOException {
    //		// 번거로움..
    //		//Spring -> MVC 템플릿 엔진을 통해 화면에 내용을 출력함.
    //		// View -> HTML => 웹페이지 or View -> json => API
    //		response.setContentType("text/html;charset=UTF-8"); // 한글로 표시하기 위함
    //		PrintWriter printWriter = response.getWriter();
    //		String msg = "<html>" + "<body>aaaa 안녕하세 요 </body>" + "</html>";
    //
    //		printWriter.write(msg);
    //		printWriter.close();
    //	}

    }
}
