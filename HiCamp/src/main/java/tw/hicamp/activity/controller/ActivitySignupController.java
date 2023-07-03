package tw.hicamp.activity.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.servlet.http.HttpSession;
import tw.hicamp.activity.dto.ActivityDtoForBackEndPage;
import tw.hicamp.activity.dto.ActivitySignupOrderDto;
import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivitySignup;
import tw.hicamp.activity.service.ActivityPeriodService;
import tw.hicamp.activity.service.ActivityService;
import tw.hicamp.activity.service.ActivitySignupService;
import tw.hicamp.member.model.Member;
import tw.hicamp.member.service.MemberService;

@Controller
public class ActivitySignupController {
	
	@Autowired
	private ActivityService aService;
	
	@Autowired
	private ActivitySignupService actSignupService;
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private ActivityPeriodService actPeriodService;
	
	@GetMapping("activity/test")
	public String testOrder() {
		return "activity/manageSignupOrderList";
	}
	
	@GetMapping("activity/testplaceOrder")
	public String testPlaceOrder() {
		return "activity/memberPlaceSignupOrder";
	}
	
//	後臺增刪改查=================================================================================
	@ResponseBody
//	新增: 手動幫客戶新增報名訂單:  postman ok / 
	@PostMapping("activity/insertOrder")
	public String insertSignupOrder(
			@RequestParam("memberNo") Integer memberNo,
			@RequestParam("activityPeriodNo") Integer activityPeriodNo,
			@RequestParam("signupDate") Date signupDate,
			@RequestParam("signupTotalAmount") Integer signupTotalAmount,
			@RequestParam("signupPaymentStatus") String signupPaymentStatus ,
			Model model) 	{
		
		    ActivitySignup activitySignup = new ActivitySignup();
		    
		    activitySignup.setMemberNo(memberNo);
		    activitySignup.setActivityPeriodNo(activityPeriodNo);
		    activitySignup.setSignupDate(signupDate);
		    activitySignup.setSignupTotalAmount(signupTotalAmount);
		    activitySignup.setSignupPaymentStatus(signupPaymentStatus);
		
		    actSignupService.insertActivitySignup(activitySignup);
		    model.addAttribute("activitySignup", activitySignup);
		    return "insert successful";
	}
	
//	查詢(全部): 訂單總表 (包含: 訂單編號: 報名日期: 會員編號: 會員姓名: 活動名稱: 活動期別: 活動單價: 報名數量: 訂單總額: 付款狀態: )
//	 postman ok 
	@GetMapping("activity/findAllSignupOrders")
	public String findAllSignupOrders(ActivitySignup activitySignup, Model model) {
		List<ActivitySignup> signupOrders = actSignupService.findAllSignupOrders();
		model.addAttribute("signupOrders", signupOrders);
		return "activity/showAllSignupOrders";
	}
	
	
//	查詢(全部) DTO  OK	<!-- (包含: 訂單編號: 報名日期:  / 會員編號: 會員姓名: / 活動編號(隱藏): 活動名稱: / 期別編號(隱藏): 出發日期: 回程日期: /訂單金額: 付款狀態: ) -->
	
	@GetMapping("/activity/findAllSignupOrdersDto")
	public String findAllSignupOrdersDto(ActivitySignupOrderDto activitySignupOrderDto, Model model) {
		
				
        List<Member> memberList = memberService.findAllMember();
		List<Activity> activityList = aService.findAllActivity();
		List<ActivityPeriod> activityPeriodList = actPeriodService.findAllActPeriods();
		List<ActivitySignup> activitySignupList = actSignupService.findAllSignupOrders();
		
		List<ActivitySignupOrderDto> signupOrderList = new ArrayList<>();

		for (ActivitySignup activitySignup : activitySignupList) {
			ActivitySignupOrderDto signupOrderDto = new ActivitySignupOrderDto();
			
			signupOrderDto.setActivitySignupNo(activitySignup.getActivitySignupNo());
			signupOrderDto.setSignupDate(activitySignup.getSignupDate());
			signupOrderDto.setSignupTotalAmount(activitySignup.getSignupTotalAmount());
			signupOrderDto.setSignupPaymentStatus(activitySignup.getSignupPaymentStatus());
			
			
			for(Member member : memberList) {
				signupOrderDto.setMemberNo(member.getMemberNo());
				signupOrderDto.setMemberName(member.getMemberName());
				break;
			}
			
			for(Activity activity : activityList) {
				signupOrderDto.setActivityNo(activity.getActivityNo());
				signupOrderDto.setActivityName(activity.getActivityName());
				break;
			}

			for (ActivityPeriod activityPeriod : activityPeriodList) {
		        	signupOrderDto.setActivityPeriodNo(activityPeriod.getActivityPeriodNo());
		        	signupOrderDto.setActivityDepartureDate(activityPeriod.getActivityDepartureDate());
		        	signupOrderDto.setActivityReturnDate(activityPeriod.getActivityReturnDate());
		            break;  
	        }
			signupOrderList.add(signupOrderDto);
		}
		System.out.println(signupOrderList);
		model.addAttribute("signupOrderList", signupOrderList);

		return "activity/manageSignupOrderList";

	}
	
	
//	查詢(單筆): 訂單細項 (包含: 訂單資訊(訂單編號/報名日期/訂單總額/付款狀態)  PS. 一個人只會產生一張訂單, 不同人會有兩張訂單 (可以歸在同一個會員底下, 但若需幫家人報名必須填個資) 
//	                     會員資訊(姓名/電話/地址/手機/Email/身分證字號/生日(西元)/緊急聯絡人/緊急聯絡人電話)
//	                     活動資訊(活動名稱/活動地點/期別編號/活動期別/活動照片(小圖)/出發日期/回程日期/活動單價)

//	 postman ok 
	@GetMapping("activity/findOneSignupOrder")
	public String findOneSignupOrder(@RequestParam("activitySignupNo") Integer activitySignupNo, Model model) {
		ActivitySignup signupOrder = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		model.addAttribute("signupOrder", signupOrder);
		return "activity/showSingupOrder";
	}
	
	@ResponseBody
	@PutMapping("activity/updateSignupOrder")
	public String editOneSignupOrder(@RequestBody ActivitySignup activitySignup, Model model) {
		
		actSignupService.updateActivitySignupOrderByNo(activitySignup.getActivitySignupNo(), activitySignup.getMemberNo(), activitySignup.getActivityPeriodNo(),
				activitySignup.getSignupDate(), activitySignup.getSignupTotalAmount(), activitySignup.getSignupPaymentStatus());
		
		model.addAttribute("activitySignup", activitySignup);
	
		return "activity/updateSignupOrder";
	}
	
//	刪除 postman ok 
	@ResponseBody
	@DeleteMapping("activity/deleteSignupOrder")
	public String deleteSignupOrder(@RequestParam("activitySignupNo") Integer activitySignupNo) {
		actSignupService.deleteActSignupOrderBySignupNo(activitySignupNo);
		return "刪除成功";
	}
	
//	前台報名活動=================================================================================
	
	//會員中心
//	@GetMapping("/membercenter")
//	public String memberCenter() {
//		return "member/memberCenter";
//	}
	
//	報名活動 判斷會員是否已登入->已登入:新增一筆訂單, 未登入:跳轉登入頁面,登入後再跳轉回來
	
	@PostMapping("activity/placeOrder")
	public String placeOrder(@RequestBody ActivitySignup activitySignup, Model model ,HttpSession session) {
		
	 	Integer memberNo = (Integer) session.getAttribute("memberNo");
	 	Integer activityPeriodNo = (Integer) session.getAttribute("activityPeriodNo");
	    
	 	Member member = memberService.findByNo(memberNo);
	 	ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);
	 	
	    ActivitySignup activitySignupOrder = actSignupService.insertActivitySignup(activitySignup);
	    
	    model.addAttribute("member", member);
	    model.addAttribute("activityPeriod", activityPeriod);
	    model.addAttribute("activitySignupOrder", activitySignupOrder);
		
	    return "activity/memberPlaceSignupOrder";
	}
//		Integer memberNo = session.getAttribute("memberNo");
//		if(memberNo == null) {
//		}
//	
	

	
	
// 會員中心
	
	
// 查詢活動訂單
   @ResponseBody
   @GetMapping("activity/activityCenterFindAllSignupOrders")
   public String findAllSignupOrdersByMemberId(@RequestParam("memberNo") Integer memberNo, Model model) {
	   ActivitySignup signupOrders = actSignupService.findSignupOrdersBymemberNo(memberNo);
	   model.addAttribute("signupOrders", signupOrders);
	   return "activity/signupOrderList";
   }
   
// 查詢單筆活動訂單
   @GetMapping("activity/activityCenterFindSignupOrder")
   public String findSignupOrderBySignupNo(@RequestParam("activitySignupNo") Integer activitySignupNo, Model model) {
		ActivitySignup signupOrder = actSignupService.findActivitySignupOrdersBySignupNo(activitySignupNo);
		model.addAttribute("signupOrder", signupOrder);
		return "activity/showSignupOrderInActivityCenter";
	}
   
   
// 取消訂單(修改訂單狀態, 未付款的訂單狀態可以用前端判斷即可, 已付款則訂單成立. )
   
   
   
   
// 付款後取消
   
   
	
// 重新下單(新增)
	
	

}
