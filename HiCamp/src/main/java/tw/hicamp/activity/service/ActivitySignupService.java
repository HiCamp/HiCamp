package tw.hicamp.activity.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivitySignup;
import tw.hicamp.activity.model.ActivitySignupRepository;

@Service
public class ActivitySignupService {

	@Autowired
	private ActivitySignupRepository actSignupRepo;
	
	public ActivitySignup insertActivitySignup(ActivitySignup activitySignup) {
		return actSignupRepo.save(activitySignup);
	}
	
	public List<ActivitySignup> findAllSignupOrders(){
		return actSignupRepo.findAll();
	}
	
	public ActivitySignup findActivitySignupOrdersBySignupNo(Integer activitySignupNo){
		Optional<ActivitySignup> optional = actSignupRepo.findById(activitySignupNo);
		if(optional.isPresent()){
			return optional.get();
	    }
		return null;
	}
	
	public ActivitySignup findSignupOrdersBymemberNo(Integer memberNo) {
		Optional<ActivitySignup> optional = actSignupRepo.findById(memberNo);
		if (optional.isPresent()) {
			return optional.get();
		}
		return null;
	}
	
	@Transactional
	public ActivitySignup updateActivitySignupOrderByNo(Integer activitySignupNo, Integer memberNo, Integer activityPeriodNo,
			Date signupDate, Integer signupTotalAmount, String signupPaymentStatus) {
		Optional<ActivitySignup> optional = actSignupRepo.findById(activitySignupNo);
		if(optional.isPresent()) {
			ActivitySignup activitySignup = optional.get();
			
//			activitySignup.setActivitySignupNo(activitySignupNo);
			activitySignup.setMemberNo(memberNo);
			activitySignup.setActivityPeriodNo(activityPeriodNo);
			activitySignup.setSignupDate(signupDate);
			activitySignup.setSignupTotalAmount(signupTotalAmount);
			activitySignup.setSignupPaymentStatus(signupPaymentStatus);
			
			return activitySignup;
		}
		return null;
	}
	
	
	public void deleteActSignupOrderBySignupNo(Integer activitySignupNo) {
		actSignupRepo.deleteById(activitySignupNo);
	}
	
	public ActivitySignupService() {
	}
	
	
}
