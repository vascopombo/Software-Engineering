package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel extends Hotel_Base{

	static final int CODE_SIZE = 7;

	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);
		
		setRoot(FenixFramework.getDomainRoot());
		
		setCode(code);
		setName(name);
		
	}
	
	public void delete() {
		setRoot(null);

		deleteDomainObject();
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getCode().equals(code)) {
				throw new HotelException();
			}
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		if (type == null || arrival == null || departure == null) {
			throw new HotelException();
		}

		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	public Set<Room> getAvailableRooms(LocalDate arrival, LocalDate departure) {
		Set<Room> availableRooms = new HashSet<>();
		for (Room room : this.rooms) {
			if (room.isFree(room.getType(), arrival, departure)) {
				availableRooms.add(room);
			}
		}
		return availableRooms;
	}

	
	public static Hotel getHotelByCode(String code) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			if (hotel.getCode().equals(code)) {
				return hotel;
			}
		}
		return null;
	}

	void addRoom(Room room) {
		if (hasRoom(room.getNumber())) {
			throw new HotelException();
		}

		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public boolean hasRoom(String number) {
		for (Room room : this.rooms) {
			if (room.getNumber().equals(number)) {
				return true;
			}
		}
		return false;
	}

	private Booking getBooking(String reference) {
		for (Room room : this.rooms) {
			Booking booking = room.getBooking(reference);
			if (booking != null) {
				return booking;
			}
		}
		return null;
	}

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}

	public static String cancelBooking(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			Booking booking = hotel.getBooking(reference);
			if (booking != null) {
				return booking.cancel();
			}
		}
		throw new HotelException();
	}

	public static RoomBookingData getRoomBookingData(String reference) {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			for (Room room : hotel.rooms) {
				Booking booking = room.getBooking(reference);
				if (booking != null) {
					return new RoomBookingData(room, booking);
				}
			}
		}
		throw new HotelException();
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		if (number < 1) {
			throw new HotelException();
		}

		List<Room> rooms = getAvailableRooms(number, arrival, departure);
		if (rooms.size() < number) {
			throw new HotelException();
		}

		Set<String> references = new HashSet<>();
		for (int i = 0; i < number; i++) {
			references.add(rooms.get(i).reserve(rooms.get(i).getType(), arrival, departure).getReference());
		}

		return references;
	}

	static List<Room> getAvailableRooms(int number, LocalDate arrival, LocalDate departure) {
		List<Room> availableRooms = new ArrayList<>();
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			availableRooms.addAll(hotel.getAvailableRooms(arrival, departure));
			if (availableRooms.size() >= number) {
				return availableRooms;
			}
		}
		return availableRooms;
	}

}
