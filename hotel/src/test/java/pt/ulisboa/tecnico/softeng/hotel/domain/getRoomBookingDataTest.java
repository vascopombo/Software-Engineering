package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class getRoomBookingDataTest {

	
	Hotel hotel;
	Room room;
	Booking booking;
	LocalDate arrival;
	LocalDate departure;
	

	@Before
	public void setUp() {
		arrival = new LocalDate(2016, 12, 19);
		departure = new LocalDate(2016,12, 21);
		
		this.hotel = new Hotel("XPTO123", "Lisboa");
		this.room = new Room(this.hotel, "01", Type.DOUBLE);
		this.booking = new Booking(this.hotel, arrival, departure);
		
	}
	
	
	@Test
	public void success() {
		

		String reference = this.booking.getReference();
		RoomBookingData data = Hotel.getRoomBookingData(reference);
		

		Assert.assertEquals(this.booking.getReference(), data.getReference());
		Assert.assertEquals(null, data.getCancellation());
		Assert.assertEquals(this.hotel.getName(), data.getHotelName());
		Assert.assertEquals(this.hotel.getCode(), data.getHotelCode());
		Assert.assertEquals(this.room.getNumber(), data.getRoomNumber());
		Assert.assertEquals(this.room.getType(), data.getRoomType());
		Assert.assertEquals(this.booking.getArrival(), data.getArrival());
		Assert.assertEquals(this.booking.getDeparture(), data.getDeparture());
		Assert.assertEquals(null, data.getCancellationDate());

	}
	
	
	@Test
	public void setTest() {
		

		String reference = this.booking.getReference();
		RoomBookingData data = Hotel.getRoomBookingData(reference);
		
		data.setReference("XPTO1231");
		data.setCancellation(null);
		data.setHotelName("Lisboa");
		data.setHotelCode("XPTO123");
		data.setRoomNumber("01");
		data.setRoomType("DOUBLE");
		data.setArrival(new LocalDate(10, 12, 2016));
		data.setDeparture(new LocalDate(15, 12, 2016));
		data.setCancellationDate(new LocalDate(8, 12, 2016));

		Assert.assertEquals("XPTO1231", data.getReference());
		Assert.assertEquals(null, data.getCancellation());
		Assert.assertEquals("Lisboa", data.getHotelName());
		Assert.assertEquals("XPTO123", data.getHotelCode());
		Assert.assertEquals("01", data.getRoomNumber());
		Assert.assertEquals("DOUBLE", data.getRoomType().toString());
		Assert.assertEquals(new LocalDate(10, 12, 2016), data.getArrival());
		Assert.assertEquals(new LocalDate(15, 12, 2016), data.getDeparture());
		Assert.assertEquals(new LocalDate(8, 12, 2016), data.getCancellationDate());

	}
	
	
	@Test(expected = HotelException.class)
	public void nullReference() {
		Hotel.getRoomBookingData(null);
	}

	@Test(expected = HotelException.class)
	public void emptyReference() {
		Hotel.getRoomBookingData("");
	}

	@Test(expected = HotelException.class)
	public void blankReference() {
		Hotel.getRoomBookingData("    ");
	}
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}
