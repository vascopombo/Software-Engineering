package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.joda.time.LocalDate;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class CancelBookingMethodTest{
	private Hotel hotel;
	private Room room;
	private String roomConfirmation;
	private LocalDate arrival;
	private LocalDate departure;

	@Before
	public void setUp(){
		arrival = new LocalDate(2017, 10, 24);
		departure = new LocalDate(2017, 11, 07);
		hotel = new Hotel("FOO1234", "Foo Hotel");
		room = new Room(hotel, "1", Type.DOUBLE);
		roomConfirmation = hotel.reserveRoom(Type.DOUBLE, arrival, departure);
	}

	@After
	public void tearDown(){
		Hotel.hotels.clear();
	}

	@Test
	public void success(){
		RoomBookingData data = hotel.getRoomBookingData(roomConfirmation);
		Assert.assertNull(data.getCancellation());
		Assert.assertNull(data.getCancellationDate());
		String result = hotel.cancelBooking(roomConfirmation);
		data = hotel.getRoomBookingData(roomConfirmation);
		Assert.assertNotNull(data.getCancellation());
		Assert.assertNotNull(data.getCancellationDate());
		Assert.assertNotNull(result);
	}

	@Test(expected = HotelException.class)
	public void nullReferenceTest(){
		hotel.cancelBooking(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReferenceTest(){
		hotel.cancelBooking("");
	}

	@Test(expected = HotelException.class)
	public void wrongReferenceTest(){
		hotel.cancelBooking("wrongReference");
	}

	@Test(expected = HotelException.class)
	public void alreadyCancelledTest(){
		hotel.cancelBooking(roomConfirmation);
		hotel.cancelBooking(roomConfirmation);
	}
}