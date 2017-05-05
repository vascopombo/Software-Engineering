package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;

@Controller
@RequestMapping(value = "/providers/{providerCode}/activities")
public class ActivityController {
	
	private static Logger logger = LoggerFactory.getLogger(ActivityController.class);
	
	@RequestMapping(method = RequestMethod.GET)
	public String showActivities(Model model, @PathVariable String providerCode) {
		logger.info("showActivities code:{}", providerCode);

		ActivityProviderData providerData = ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES);

		if (providerData == null) {
			model.addAttribute("error", "Error: it does not exist a broker with the code " + providerCode);
			model.addAttribute("provider", new ActivityProviderData());
			model.addAttribute("providers", ActivityInterface.getProviders());
			return "providers";
		} else {
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("provider", providerData);
			return "activities";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitActivity(Model model, @PathVariable String providerCode,
			@ModelAttribute ActivityData activityData) {
		logger.info("activitySubmit providerCode:{}, name:{}, code:{}, minAge:{}, maxAge:{}, capacity:{}", providerCode,
				activityData.getName(), activityData.getCode(), activityData.getMinAge(), activityData.getMaxAge(), activityData.getCapacity());

		try {
			ActivityInterface.createActivity(providerCode, activityData);
		} catch (ActivityException be) {
			model.addAttribute("error", "Error: it was not possible to create the activity");
			model.addAttribute("activity", activityData);
			model.addAttribute("activityProvider", ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES));
			return "activities";
		}

		return "redirect:/providers/" + providerCode + "/activities";
	}
}
