package pt.ulisboa.tecnico.softeng.activity.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.activity.services.local.ActivityInterface;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityData;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityProviderData.CopyDepth;
import pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects.ActivityReservationData;


@Controller
@RequestMapping(value = "/proiders/{providerCode}/activities/{activityCode}/offers/{offerCode}")
public class ActivityReservationController {
	private static Logger logger = LoggerFactory.getLogger(ActivityReservationController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBookings(Model model, @PathVariable String providerCode, @PathVariable String activityCode, @PathVariable String offerCode) {
		logger.info("showOperations providerCode:{} activityCode:{} offerCode{}", providerCode, activityCode, offerCode);

		ActivityData activityData = ActivityInterface.getActivityDataByCode(activityCode);
		
		if (activityData == null) {
			model.addAttribute("error", "Error: it does not exist a activity with the code " + activityCode);
			model.addAttribute("activity", new ActivityData());
			model.addAttribute("activities", ActivityInterface.getActivities());
			return "providers";
		} else {
			model.addAttribute("booking", new ActivityReservationData());
			model.addAttribute("provider", ActivityInterface.getActivityProviderDataByCode(providerCode, CopyDepth.ACTIVITIES));
			model.addAttribute("activity",activityData);
			return "bookings";
		}
	}
}
