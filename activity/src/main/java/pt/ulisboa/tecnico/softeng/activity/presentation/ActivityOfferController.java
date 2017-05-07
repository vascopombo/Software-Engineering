package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityOfferData;

import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/providers/{providerCode}/activities/{activityCode}/offers")
public class ActivityOfferController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);
	
	
	@RequestMapping(method = RequestMethod.GET)
	public String showOffers(Model model, @PathVariable String activityCode) {
		logger.info("showOffers activityCode:{}",activityCode);
		
		ActivityData activityData = ActivityInterface.getActivityDataByCode(activityCode);
		
		if (activityData == null) {
			model.addAttribute("error", "Error: it does not exist a activity with the code " + activityCode);
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("activities", ActivityInterface.getActivities());
			return "providers";
		} else {
			model.addAttribute("offer", new ActivityOfferData());
			model.addAttribute("activity", activityData);
			return "offers";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitOffer(Model model, @PathVariable String providerCode, 
			@PathVariable String activityCode, @ModelAttribute ActivityOfferData activityOfferData) {
		
		logger.info("submitOffer providerCode:{}, activityCode:{}, begin:{}, end:{}", providerCode, activityCode, activityOfferData.getBegin(), 
				activityOfferData.getEnd());
		
		
		try{
			ActivityInterface.createOffer(activityCode, activityOfferData);
		}catch(ActivityException ae){
			model.addAttribute("error", "Error: it was not possible to create the offer");
			model.addAttribute("offer", activityOfferData);
			model.addAttribute("activity", ActivityInterface.getActivityDataByCode(activityCode));
			return "offers";			
		}
		return "redirect:/provider/" + providerCode + "/activities/"+ activityCode + "/offers";
	}
	
	
}