package pt.ulisboa.tecnico.softeng.hotel.domain;

import java.util.HashSet;
import java.util.Set;

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


public class HotelBulkBookingMethodTest {

	Hotel hotel1, hotel2;
	Room room1, room2, room3, room4, room5;
	Booking booking;
	LocalDate arrival, departure;
	String reference;
	Set<String> references;
	RoomBookingData data;
	

	@Before
	public void setUp() {		
		references = new HashSet<String>();
		this.data = new RoomBookingData();		
		arrival = new LocalDate(2016, 12, 19);
		departure = new LocalDate(2016, 12, 21);		
		this.hotel1 = new Hotel("HOTEL1 ", "Lisboa");
		this.hotel2 = new Hotel("HOTEL2 ", "Porto");
		this.room1 = new Room(this.hotel1, "11", Type.DOUBLE);
		this.room2 = new Room(this.hotel1, "12", Type.SINGLE);
		this.room3 = new Room(this.hotel2, "23", Type.SINGLE);
		this.room4 = new Room(this.hotel2, "24", Type.DOUBLE);
		this.room5 = new Room(this.hotel2, "25", Type.SINGLE);		
	}
	
	
	@Test
	public void successWithoutPreviousBookings() {
		
		Assert.assertEquals(references.size(), 0);
		Assert.assertTrue(room1.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertTrue(room2.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room4.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertTrue(room5.isFree(Type.SINGLE,arrival,departure));
		references = Hotel.bulkBooking(5, arrival, departure);
		Assert.assertEquals(references.size(), 5);
		for(String s: references){
			RoomBookingData data = Hotel.getRoomBookingData(s);
			Assert.assertEquals(data.getArrival(),arrival);
			Assert.assertEquals(data.getDeparture(),departure);
		}
		Assert.assertFalse(room1.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertFalse(room2.isFree(Type.SINGLE,arrival,departure));
		Assert.assertFalse(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertFalse(room4.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertFalse(room5.isFree(Type.SINGLE,arrival,departure));				
	}

	@Test
	public void successWithOneRoomBooked() {
		
		room5.reserve(Type.SINGLE, arrival, departure);
		Assert.assertTrue(room1.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertTrue(room2.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room4.isFree(Type.DOUBLE,arrival,departure));
		references = Hotel.bulkBooking(4, arrival, departure);
		Assert.assertEquals(references.size(), 4);
		for(String s: references){
			RoomBookingData data = Hotel.getRoomBookingData(s);
			Assert.assertEquals(data.getArrival(),arrival);
			Assert.assertEquals(data.getDeparture(),departure);
			Assert.assertNotEquals(data.getRoomNumber(), "25");
		}
		Assert.assertFalse(room1.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertFalse(room2.isFree(Type.SINGLE,arrival,departure));
		Assert.assertFalse(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertFalse(room4.isFree(Type.DOUBLE,arrival,departure));
	}	
	
	@Test
	public void successWithOneHotelFullyBooked() {
		
		room1.reserve(Type.DOUBLE, arrival, departure);
		room2.reserve(Type.SINGLE, arrival, departure);
		Assert.assertTrue(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room4.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertTrue(room5.isFree(Type.SINGLE,arrival,departure));
		references = Hotel.bulkBooking(3, arrival, departure);
		Assert.assertEquals(references.size(), 3);
		for(String s: references){
			RoomBookingData data = Hotel.getRoomBookingData(s);
			Assert.assertEquals(data.getArrival(),arrival);
			Assert.assertEquals(data.getDeparture(),departure);
			Assert.assertEquals(data.getHotelCode(),"HOTEL2 ");
			Assert.assertEquals(data.getHotelName(),"Porto");
		}
		Assert.assertFalse(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertFalse(room4.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertFalse(room5.isFree(Type.SINGLE,arrival,departure));
	}
	
	//TODO : DESCOMENTAR DEPOIS DE CANCELBOOKING ESTAR FEITO
	/*@Test
	public void checkReservesGotCancelled() {
		
		room1.reserve(Type.DOUBLE, arrival, departure);
		Assert.assertTrue(room2.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room4.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertTrue(room5.isFree(Type.SINGLE,arrival,departure));
		try{
			references = Hotel.bulkBooking(5, arrival, departure);
		} catch(HotelException he){
		}
		Assert.assertTrue(room2.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room3.isFree(Type.SINGLE,arrival,departure));
		Assert.assertTrue(room4.isFree(Type.DOUBLE,arrival,departure));
		Assert.assertTrue(room5.isFree(Type.SINGLE,arrival,departure));
	}*/
	
	
	@Test(expected = HotelException.class)
	public void zeroRooms() {
		Hotel.bulkBooking(0, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void NegativeRooms() {
		Hotel.bulkBooking(-20, arrival, departure);
	}

	@Test(expected = HotelException.class)
	public void departureBeforeArrival() {
		Hotel.bulkBooking(0, departure, arrival);
	}

	@Test(expected = HotelException.class)
	public void arrivalEqualDeparture() {
		Hotel.bulkBooking(0, arrival, arrival);
	}
	
	@Test(expected = HotelException.class)
	public void nullArrival() {
		Hotel.bulkBooking(3, null, this.departure);
	}

	@Test(expected = HotelException.class)
	public void nullDeparture() {
		Hotel.bulkBooking(3, this.arrival, null);
	}
	
	@Test(expected = HotelException.class)
	public void nullBoth() {
		Hotel.bulkBooking(3, null, null);
	}
	
	@Test(expected = HotelException.class)
	public void AllArgumentsWrong() {
		Hotel.bulkBooking(-1, null, null);
	}
	
	@Test(expected = HotelException.class)
	public void TooMuchRooms() {
		Hotel.bulkBooking(6, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void TooMuchRoomsWithOneAlreadyBooked() {
		room1.reserve(Type.DOUBLE, arrival, departure);
		Hotel.bulkBooking(5, arrival, departure);
	}
	
	
	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}
	
}

