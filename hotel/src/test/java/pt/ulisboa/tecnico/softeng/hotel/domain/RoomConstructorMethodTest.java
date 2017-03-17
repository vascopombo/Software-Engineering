package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomConstructorMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
	}

	@Test
	public void success() {
		Room room = new Room(this.hotel, "01", Type.DOUBLE);

		Assert.assertEquals(this.hotel, room.getHotel());
		Assert.assertEquals("01", room.getNumber());
		Assert.assertEquals(Type.DOUBLE, room.getType());
		Assert.assertEquals(1, this.hotel.getNumberOfRooms());
	}
	
	@Test(expected = HotelException.class)
	public void uniqueRoomId(){
		new Room(this.hotel, "02", Type.DOUBLE);
		new Room(this.hotel, "02", Type.SINGLE);
		}
	
	@Test
	public void TwoRoomsDiferentId(){
		new Room(this.hotel, "01", Type.SINGLE);
		new Room(this.hotel, "02", Type.SINGLE);
		}
	
	@Test(expected = HotelException.class)
	public void numericChar(){
		 new Room(this.hotel, "er", Type.DOUBLE);
		}
	
	@Test(expected = HotelException.class)
	public void numericChar2(){
		new Room(this.hotel, "0,2", Type.DOUBLE);
		}
	
	@Test(expected = HotelException.class)
	public void emptyArg(){
		new Room(this.hotel, "", Type.SINGLE);
	}
	
	@Test(expected = HotelException.class)
	public void nullArgs(){
		new Room(this.hotel, null, Type.SINGLE);
	}
	
	@Test(expected = HotelException.class)
	public void nullArgs2(){
		new Room(null, "01", Type.SINGLE);
	}
	
	@Test(expected = HotelException.class)
	public void nullArgs3(){
		new Room(this.hotel, "01", null);
	}
	
	@Test(expected = HotelException.class)
	public void whitespaceArg(){
		new Room(this.hotel, "  ", Type.SINGLE);
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
