package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class RoomData {
	
	private String number;
	private Type type;
	public List<RoomBookingData> bookings = new ArrayList<>();
	
	public RoomData() {
	}

	public RoomData(Room room) {
		this.number = room.getNumber();
		this.type = room.getType();

	}

	public String getNumber() {
		return this.number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Type getType() {
		return this.type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
