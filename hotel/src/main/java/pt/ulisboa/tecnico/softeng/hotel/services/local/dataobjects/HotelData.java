package pt.ulisboa.tecnico.softeng.hotel.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;

public class HotelData {
	public static enum CopyDepth {
		SHALLOW,ROOMS
	};

	private String name;
	private String code;
	private List<RoomData> rooms = new ArrayList<>();

	public HotelData() {
	}

	public HotelData(Hotel hotel, CopyDepth depth) {
		this.name = hotel.getName();
		this.code = hotel.getCode();


		switch (depth) {
		case ROOMS:
			for (Room room : hotel.getRoomSet()) {
				this.rooms.add(new RoomData(room));
			}
			break;
		case SHALLOW:
			break;
		default:
			break;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
