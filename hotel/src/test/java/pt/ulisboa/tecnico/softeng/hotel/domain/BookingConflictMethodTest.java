package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BookingConflictMethodTest {
	Booking booking;

	@Before
	public void setUp() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);
		this.booking = new Booking(hotel, arrival, departure);
	}

	@Test
	public void noConflictBefore() {
		LocalDate arrival = new LocalDate(2016, 12, 16);
		LocalDate departure = new LocalDate(2016, 12, 19);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}

	@Test
	public void noConflictAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 24);
		LocalDate departure = new LocalDate(2016, 12, 30);

		Assert.assertFalse(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictArrivalAfterDepartureAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 25);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictInMiddle() {
		LocalDate arrival = new LocalDate(2016, 12, 20);
		LocalDate departure = new LocalDate(2016, 12, 22);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictArrivalBeforeDepartAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 18);
		LocalDate departure = new LocalDate(2016, 12, 25);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictEqualDate() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 24);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictArrivalBeforeDepartBefore() {
		LocalDate arrival = new LocalDate(2016, 12, 18);
		LocalDate departure = new LocalDate(2016, 12, 20);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictArrivalBeforeDepartEqual() {
		LocalDate arrival = new LocalDate(2016, 12, 18);
		LocalDate departure = new LocalDate(2016, 12, 24);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}
	
	@Test
	public void conflictArrivalEqualDepartAfter() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 25);

		Assert.assertTrue(this.booking.conflict(arrival, departure));
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
