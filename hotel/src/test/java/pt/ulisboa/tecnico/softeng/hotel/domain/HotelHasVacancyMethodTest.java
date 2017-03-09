package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class HotelHasVacancyMethodTest {
	private Hotel hotel;

	@Before
	public void setUp() {
		this.hotel = new Hotel("XPTO123", "Paris");
		new Room(this.hotel, "01", Type.DOUBLE);
	}

	@Test
	public void hasVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival, departure);

		Assert.assertEquals("01", room.getNumber());
	}
	
	@Test
	public void hasNoVacancy() {
		LocalDate arrival = new LocalDate(2016, 12, 19);
		LocalDate departure = new LocalDate(2016, 12, 21);
	    Hotel.reserveHotel(Type.DOUBLE, arrival, departure);
		
	    LocalDate arrival1 = new LocalDate(2016, 12, 18);
		LocalDate departure1 = new LocalDate(2016, 12, 20);

		Room room = this.hotel.hasVacancy(Type.DOUBLE, arrival1, departure1);

		Assert.assertNull(room);
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
