package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class BookingContructorMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;
	private ActivityOffer offer2;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);
		
		Activity activity2 = new Activity(this.provider, "Bush Walking", 18, 80, 1);
		
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
		
		this.offer2 = new ActivityOffer(activity2, begin, end);
	}

	@Test
	public void success() {
		Booking booking = new Booking(this.provider, this.offer);

		Assert.assertTrue(booking.getReference().startsWith(this.provider.getCode()));
		Assert.assertTrue(booking.getReference().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, this.offer.getNumberOfBookings());
	}
	
	@Test
	public void checkBookingConstructorSuccess(){
		Booking booking2 = new Booking(this.provider, offer2);
	}
	
	@Test(expected = ActivityException.class)
	public void checkBookingConstructorFailure(){
		Booking booking2 = new Booking(this.provider, offer2);
		Booking booking3 = new Booking(this.provider, offer2);
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
