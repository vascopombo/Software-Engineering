package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.fail;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;

public class BookingNewActivityReservationDataMethodTest {
	private ActivityProvider provider;
	private ActivityOffer offer;
	private ActivityReservationData data;
	private Booking booking;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
		this.booking = new Booking(this.provider, this.offer);
	}

	@Test
	public void success() {
		data = booking.newActivityReservationData(this.provider, this.offer);

		Assert.assertEquals(booking.getReference(),data.getReference());
		Assert.assertEquals(provider.getName(), data.getName());
		Assert.assertEquals(provider.getCode(), data.getCode());
		Assert.assertEquals(offer.getBegin(), data.getBegin());
		Assert.assertEquals(offer.getEnd(), data.getEnd());
	}

	@Test(expected = ActivityException.class)
	public void nullProvider() {
		booking.newActivityReservationData(null, this.offer);
	}

	@Test(expected = ActivityException.class)
	public void nullOffer() {
		booking.newActivityReservationData(this.provider, null);
	}

	@Test(expected = ActivityException.class)
	public void AllNull() {
		booking.newActivityReservationData(this.provider, null);
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
