package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Room extends Room_Base{
	public static enum Type {
		SINGLE, DOUBLE
	}


	public Room(Hotel hotel, String number, Type type) {
		checkArguments(hotel, number, type);

		setNumber(number);
		setType(type);
		if(hotel.hasRoom(number)){
			throw new HotelException();
		}

		setHotel(hotel);
		hotel.addRoom(this);
		
	}
	
	public void delete() {
		setHotel(null);
		for(Booking booking : getBookingSet()){
			booking.delete();
		}
		deleteDomainObject();
	}

	private void checkArguments(Hotel hotel, String number, Type type) {
		if (hotel == null || number == null || number.trim().length() == 0 || type == null) {
			throw new HotelException();
		}

		if (!number.matches("\\d*")) {
			throw new HotelException();
		}
	}

	boolean isFree(Type type, LocalDate arrival, LocalDate departure) {
		if (!type.equals(getType())) {
			return false;
		}

		for (Booking booking : getBookingSet()) {
			if (booking.conflict(arrival, departure)) {
				return false;
			}
		}

		return true;
	}

	public Booking reserve(Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		if (!isFree(type, arrival, departure)) {
			throw new HotelException();
		}

		Booking booking = new Booking(getHotel(), arrival, departure);
		booking.setRoom(this);

		return booking;
	}

	public Booking getBooking(String reference) {
		for (Booking booking : getBookingSet()) {
			if (booking.getReference().equals(reference)
					|| (booking.isCancelled() && booking.getCancellation().equals(reference))) {
				return booking;
			}
		}
		return null;
	}

}
