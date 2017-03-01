package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();

	public Hotel(String code, String name) {
		checkCode(code);
		checkUniqueCode(code);
		
		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkCode(String code) {
		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}
	}	
	
	//verificar que nao existe nenhum codigo igual nos hoteis ja existentes
	public void checkUniqueCode(String code) {
		for (Hotel hotel : hotels) { 
			if (hotel.code.equals(code))
				throw new HotelException();
		}
	}

	public Room hasVacancy(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Room room : this.rooms) {
			if (room.isFree(type, arrival, departure)) {
				return room;
			}
		}
		return null;
	}

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
	}

	void addRoom(Room room) {
		this.rooms.add(room);
	}

	int getNumberOfRooms() {
		return this.rooms.size();
	}

	public static String reserveHotel(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		return null;
	}

}
