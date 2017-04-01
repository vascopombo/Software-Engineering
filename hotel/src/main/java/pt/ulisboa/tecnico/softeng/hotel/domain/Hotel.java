package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Hotel {
	public static Set<Hotel> hotels = new HashSet<>();

	static final int CODE_SIZE = 7;

	private final String code;
	private final String name;
	private final Set<Room> rooms = new HashSet<>();
	private static Set<RoomBookingData> roomBookingData = new HashSet<>();

	public Hotel(String code, String name) {
		checkArguments(code, name);

		this.code = code;
		this.name = name;
		Hotel.hotels.add(this);
	}

	private void checkArguments(String code, String name) {
		if (code == null || name == null || code.trim().length() == 0 || name.trim().length() == 0) {
			throw new HotelException();
		}

		if (code.length() != Hotel.CODE_SIZE) {
			throw new HotelException();
		}

		for (Hotel hotel : hotels) {
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

	String getCode() {
		return this.code;
	}

	String getName() {
		return this.name;
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

	public static String reserveRoom(Room.Type type, LocalDate arrival, LocalDate departure) {
		for (Hotel hotel : Hotel.hotels) {
			Room room = hotel.hasVacancy(type, arrival, departure);
			if (room != null) {
				return room.reserve(type, arrival, departure).getReference();
			}
		}
		throw new HotelException();
	}
	
	public void addRoomBookingData(Hotel hotel, Room room, Booking booking){
		RoomBookingData data = new RoomBookingData();
		data.construct(booking.getReference(), hotel.getName(), hotel.getCode(), room.getNumber(), room.getType().toString(), booking.getArrival(), booking.getDeparture());
		roomBookingData.add(data);
	}

	public static String cancelBooking(String roomConfirmation) {
		RoomBookingData data = Hotel.getRoomBookingData(roomConfirmation);
		if(data.getCancellation() == null && data.getCancellationDate() == null){
			data.setCancellationDate(LocalDate.now());
			data.setCancellation("Cancelation: " + data.getReference());
			return data.getCancellation();
		}
		throw new HotelException();
	}
	

	public static RoomBookingData getRoomBookingData(String reference) {
		if (reference == null || reference.trim().length() == 0) {
			throw new HotelException();
		}
		
		RoomBookingData data = new RoomBookingData();
				
		for (Hotel hotel : Hotel.hotels){
			for (Room room : hotel.rooms){
				for (Booking booking : room.getBookings()){
					if (booking.getReference() == reference){
						data.setArrival(booking.getArrival());
						data.setCancellation(null);
						data.setCancellationDate(null);
						data.setDeparture(booking.getDeparture());
						data.setHotelCode(hotel.getCode());
						data.setHotelName(hotel.getName());
						data.setReference(booking.getReference());
						data.setRoomNumber(room.getNumber());
						data.setRoomType(room.getType().toString());
						return data;
					}
				}
				
			}
		}
		throw new HotelException();		
	}

	public static Set<String> bulkBooking(int number, LocalDate arrival, LocalDate departure) {
		if(number<=0 || arrival == null || departure == null ||
				departure.isBefore(arrival)||departure.isEqual(arrival)){
			throw new HotelException();
		}
		int i=0;
		String reserva = null;
		Set<String> referencias = new HashSet<String>();
		
		while(i<number){
			try{
				reserva = reserveRoom(Room.Type.SINGLE, arrival, departure);
			} catch(HotelException rae){
				break;
			}
			referencias.add(reserva);
			i++;
		}
		while(i<number){
			try{
				reserva = reserveRoom(Room.Type.DOUBLE, arrival, departure);
			} catch(HotelException rae){
				break;
			}
			referencias.add(reserva);
			i++;
		}
		if(i<number){
			for(String s : referencias){
				cancelBooking(s);
			}
			throw new HotelException();
		}
		return referencias;
	}
}
