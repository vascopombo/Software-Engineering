package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class RoomData {
	
	private String number;
	private pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type type;

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

	public pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type getType() {
		return this.type;
	}

	public void setType(pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type type) {
		this.type = type;
	}
}
