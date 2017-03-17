package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;


import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class BookingConstructorTest {

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Booking booking = new Booking(hotel, arrival, departure);

		Assert.assertTrue(booking.getReference().startsWith(hotel.getCode()));
		Assert.assertTrue(booking.getReference().length() > Hotel.CODE_SIZE);
		Assert.assertEquals(arrival, booking.getArrival());
		Assert.assertEquals(departure, booking.getDeparture());
	}
	
	@Test(expected = HotelException.class)
	public void dateError() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 30);
		LocalDate departure = new LocalDate(2016, 12, 21);

			new Booking(hotel, arrival, departure);
	}
	
	@Test
	public void dateCorrect() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 22);
		LocalDate departure = new LocalDate(2016, 12, 30);

			new Booking(hotel, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullArg(){
		
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 22);
		LocalDate departure = new LocalDate(2016, 12, 30);
		
		new Booking (null, arrival, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullArg2(){
		
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 22);
		LocalDate departure = new LocalDate(2016, 12, 30);
		
		new Booking (hotel, null, departure);
	}
	
	@Test(expected = HotelException.class)
	public void nullArg3(){
		
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 22);
		LocalDate departure = new LocalDate(2016, 12, 30);
		
		new Booking (hotel, arrival, null);
	}
	
	

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
