package tw.hicamp.activity.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.LinkedList;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import tw.hicamp.activity.dto.ActivityDto;
import tw.hicamp.activity.model.Activity;
import tw.hicamp.activity.model.ActivityPeriod;
import tw.hicamp.activity.model.ActivityPicture;
import tw.hicamp.activity.service.ActivityPeriodService;
import tw.hicamp.activity.service.ActivityPictureService;
import tw.hicamp.activity.service.ActivityService;

@Controller
public class ActivityPeriodController {

	@Autowired
	private ActivityService aService;

	@Autowired
	private ActivityPictureService actPicService;

	@Autowired
	private ActivityPeriodService actPeriodService;

	@GetMapping("/activity/testteeeeeeeeeest")
	public String test() {
		return "activity/testpage";
	}

//	查詢單筆期別
	@GetMapping("/activity/findActivityPeriodById")
	public String findActivityByActivityPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo, Model model) {
		
		ActivityPeriod activityPeriod = actPeriodService.findActPeriodById(activityPeriodNo);
		
		model.addAttribute("activityPeriod", activityPeriod);
		return "activity/testActivityPeriod";
	}
	
	@DeleteMapping("/activity/deleteActivityPeriod")
	public String deleteActivityPeriod(@RequestParam("activityPeriodNo") Integer activityPeriodNo) {
		actPeriodService.deleteActPeriodById(activityPeriodNo);
		return "刪除成功";
	}

}