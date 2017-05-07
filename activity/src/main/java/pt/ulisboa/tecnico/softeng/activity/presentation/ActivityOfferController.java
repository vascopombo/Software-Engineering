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
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;

import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequestMapping(value = "/providers/{providerCode}/activities/{activityCode}/offers")
public class ActivityOfferController {
	private static Logger logger = LoggerFactory.getLogger(ActivityOfferController.class);
	
	@RequestMapping(method = RequestMethod.POST)
	public String submitOffer(Model model, @PathVariable String providerCode, 
			@PathVariable String activityCode, @ModelAttribute ActivityOfferData activityOfferData) {
		
		logger.info("offerSubmit providerCode:{}, activityCode:{]}, begin:{}, end:{}, capacity:{}", providerCode, activityCode, activityOfferData.getBegin(), 
				activityOfferData.getEnd(), activityOfferData.getCapacity());
		
		
		try{
			ActivityInterface.createOffer(providerCode, activityCode, activityOfferData);
		}catch(ActivityException ae){
			model.addAttribute("error", "Error: it was not possible to create the offer");
			model.addAttribute("offer", activityOfferData);
			return "offers";			
		}
		return "redirect:/provider/" + providerCode + "/activity/"+ activityCode + "/offers";
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public String showOffers(Model model, @PathVariable String providerCode, 
			@PathVariable String activityCode) {
		
		logger.info("showOffers providerCode:{}, activityCode:{}",providerCode, activityCode);
		
		ActivityProviderData activityProviderData = ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES);
		
		for(ActivityData activityData : activityProviderData.getActivities()) {
			if(activityCode.equals(activityData.getCode())) {
				model.addAttribute("offer", new ActivityOfferData());
				model.addAttribute("activity", activityData);
				model.addAttribute("provider", activityProviderData);
				return "offers";
			}
		}
		model.addAttribute("error", "Error: it does not exist a activity with the code " + activityCode);
		model.addAttribute("activity", new ActivityData());
		model.addAttribute("provider", ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES));
		return "activities";
	}
}