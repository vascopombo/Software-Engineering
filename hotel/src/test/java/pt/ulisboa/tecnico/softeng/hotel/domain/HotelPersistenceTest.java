package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class HotelPersistenceTest {
	
	private static final String HOTEL_CODE = "XPTO123";
	private static final String HOTEL_NAME = "Londres";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Hotel hotel1 = new Hotel(HOTEL_CODE, HOTEL_NAME);
		Room room1 = new Room(hotel1, "01", Type.SINGLE);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());

		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());
		Hotel hotel = hotels.get(0);

		assertEquals(HOTEL_CODE, hotel.getCode());
		assertEquals(HOTEL_NAME, hotel.getName());
		
		assertEquals(1, hotel.getRoomSet().size());

		List<Room> rooms = new ArrayList<>(hotel.getRoomSet());
		
		Room room = rooms.get(0);

		assertEquals("01", room.getNumber());
		assertEquals(Type.SINGLE, room.getType());
		assertEquals(HOTEL_CODE, room.getHotel().getCode());
		assertEquals(HOTEL_NAME, room.getHotel().getName());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
