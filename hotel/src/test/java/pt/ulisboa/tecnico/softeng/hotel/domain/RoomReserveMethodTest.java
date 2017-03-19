package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class RoomReserveMethodTest {
	Room room;
	Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(this.hotel, "01", Type.SINGLE);
	}

	@Test
	public void success() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		Booking booking = this.room.reserve(Type.SINGLE, arrival, departure);

		Assert.assertTrue(booking.getReference().length() > 0);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}
	
	@Test(expected = HotelException.class)
	public void noRoomAvailable() {
		
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);
	    Hotel.reserveHotel(Type.SINGLE, arrival, departure);
		
	    LocalDate arrival1 = new LocalDate(2016, 12, 18);
		LocalDate departure1 = new LocalDate(2016, 12, 20);

		this.room.reserve(Type.SINGLE, arrival1, departure1);
}
	
	@Test(expected = HotelException.class)
	public void hasNoRoomType() {
	    LocalDate arrival = new LocalDate(2016, 12, 18);
		LocalDate departure = new LocalDate(2016, 12, 20);

		this.room.reserve(Type.DOUBLE, arrival, departure);
		 
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
}
