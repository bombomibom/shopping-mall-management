package com.salt.shop;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ViewController {
	
	/** 시작 페이지 */
	@GetMapping("/")
	public String root() {
		return "index";
	}
	
	/** 헤더 */
	@GetMapping("/header")
	public String header() {
		return "header";
	}
	
	/** 푸터 */
	@GetMapping("/footer")
	public String footer() {
		return "footer";
	}
	
	/** 등록 페이지 */
	@GetMapping("/form")
	public String form(Model model, @RequestParam("mode") String mode, HttpServletRequest request) {
		
		DB<User> db = new DB<User>("MEMBER_TBL_02");
		
		if(mode.equals("insert")) {
			Date nowDate = new Date();
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMdd");
			String strNowDate = simpleDateFormat.format(nowDate);
			String userNo = db.selectCustNo("CUSTNO");
			model.addAttribute("userNo", userNo);
			model.addAttribute("nowDate", strNowDate);
			model.addAttribute("mode", "insert");
			
		} else if (mode.equals("update")) {
			ArrayList<User> list = new ArrayList<User>();
			int custNo = Integer.parseInt(request.getParameter("custNo"));
			System.out.println(custNo);
			list = db.selectThisUserArrayList(new User(custNo));
			System.out.println(list);
			model.addAttribute("userInfo", list);
			model.addAttribute("mode", "update");
		}
		
		return "form";
	}
	
	/** 회원목록조회 페이지 */
	@GetMapping("/userList")
	public String userList() {
		return "userList";
	}
	
	/** 매출조회 페이지 */
	@GetMapping("/userSales")
	public String userSales(Model model) {
		return "userSales";
	}
	
	
}
