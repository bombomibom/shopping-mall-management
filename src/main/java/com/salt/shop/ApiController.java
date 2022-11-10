package com.salt.shop;


import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ApiController {
	
	/** 유저 정보 삽입 */
	@PostMapping("insert_action")
	public String insert_action(@ModelAttribute User user, @RequestParam("strJoinDate") String strJoinDate) {
		
		int custNo = user.getCustNo();
		String custName = user.getCustName();
		String phone = user.getPhone();
		String address = user.getAddress();
		String grade = user.getGrade();
		String city = user.getCity();
		
		SimpleDateFormat beforeFormat = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date tempDate = null;
        
        try {
        	String date = strJoinDate;
            tempDate = beforeFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String transDate = afterFormat.format(tempDate);
        Date joinDate = Date.valueOf(transDate);
		System.out.println(joinDate);
        
		DB<User> db = new DB<User>("MEMBER_TBL_02");
		boolean isSuccess = db.insertData(new User(custNo, custName, phone, address, joinDate, grade, city)); 
		String msg = "";
		
		if(isSuccess) {
			msg = "회원등록이 완료 되었습니다!";
			return msg;
		} else {
			msg = "등록에 실패했습니다.";
			return msg;
		}
	}
	
	/** 유저 리스트 조회 */
	@GetMapping("/get_user_list")
	@ResponseBody
	public ArrayList<User> getUserList(Model model) {
		
		DB<User> db = new DB<User>("MEMBER_TBL_02");
		ArrayList<User> list = new ArrayList<User>();
		
		list = db.selectUserArrayList(new User());
		System.out.println(list);
		
		return list;
	}
	
	/** 유저 매출 조회 */
	@GetMapping("/get_user_sales")
	@ResponseBody
	public ArrayList<Sales> getUserSales(Model model) {
		
		DB<Sales> db = new DB<Sales>();
		ArrayList<Sales> list = new ArrayList<Sales>();
		
		list = db.selectUserSalesArrayList(new Sales());
		System.out.println(list);
		
		return list;
	}
	
	/** 유저 정보 수정 */
	@PostMapping("/update_action")
	public String update_action(@ModelAttribute User user, @RequestParam("strJoinDate") String strJoinDate) {
		
		int custNo = user.getCustNo();
		String custName = user.getCustName();
		String phone = user.getPhone();
		String address = user.getAddress();
		String grade = user.getGrade();
		String city = user.getCity();
		
		SimpleDateFormat beforeFormat = new SimpleDateFormat("yyMMdd");
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date tempDate = null;
        
        try {
        	String date = strJoinDate;
            tempDate = beforeFormat.parse(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        String transDate = afterFormat.format(tempDate);
        Date joinDate = Date.valueOf(transDate);
		System.out.println(joinDate);
        
		DB<User> db = new DB<User>("MEMBER_TBL_02");
		boolean isSuccess = db.updateData(new User(custNo, custName, phone, address, joinDate, grade, city)); 
		String msg = "";
		
		if(isSuccess) {
			msg = "회원수정이 완료 되었습니다!";
			return msg;
		} else {
			msg = "수정에 실패했습니다.";
			return msg;
		}
	}
	
}
