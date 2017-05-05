package pt.ulisboa.tecnico.softeng.hotel.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.hotel.services.local.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomData;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.HotelData.CopyDepth;
import pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects.RoomBookingData;

@Controller
@RequestMapping(value = "/hotels/{hotelCode}/rooms/{roomNumber}/bookings")
public class BookingController {
	private static Logger logger = LoggerFactory.getLogger(BookingController.class);

	@RequestMapping(method = RequestMethod.GET)
	public String showBooking(Model model, @PathVariable String roomNumber) {
		logger.info("showBookings number:{}", roomNumber);

		RoomData roomData = HotelInterface.getRoomDataByNumber(roomNumber);

		if (roomData == null) {
			model.addAttribute("error", "Error: it does not exist a hotel with the code " + roomNumber);
			model.addAttribute("room", new RoomData());
			model.addAttribute("rooms", HotelInterface.getRooms());
			return "hotels";
		} else {
			model.addAttribute("booking", new RoomBookingData());
			model.addAttribute("room", roomData);
			return "bookings";
		}
	}

	@RequestMapping(method = RequestMethod.POST)
	public String submitBooking(Model model, @PathVariable String hotelCode, @PathVariable String roomNumber,
			@ModelAttribute RoomBookingData bookingData) {
		logger.info("bookingSubmit roomNumber:{}, arrival:{}, departure:{}", roomNumber,
				bookingData.getArrival(), bookingData.getDeparture());

		try {
			HotelInterface.createBooking(roomNumber, bookingData);
		} catch (HotelException be) {
			model.addAttribute("error", "Error: it was not possible to create the room");
			model.addAttribute("booking", bookingData);
			model.addAttribute("room", HotelInterface.getRoomDataByNumber(roomNumber));
			return "bookings";
		}

		return "redirect:/hotels/" + hotelCode + "/rooms/"+ roomNumber + "/bookings";
	}

}
