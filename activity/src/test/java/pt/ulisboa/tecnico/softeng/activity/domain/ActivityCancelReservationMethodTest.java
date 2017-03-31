package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;

public class ActivityCancelReservationMethodTest {
	
	private Activity activity;
	private ActivityProvider provider;
	private ActivityOffer offer;
	private Booking booking;
	private ActivityReservationData data;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(provider, "XtremX", 20, 25, 10);
		this.offer = new ActivityOffer(activity, begin, end);
		this.booking = new Booking(provider, offer);
	}

	@Test
	public void success() {
		String reference = this.booking.getReference();
		String cancel = ActivityProvider.cancelReservation(reference);
		data = ActivityProvider.getActivityReservationData(reference);
		
		Assert.assertNotNull(data.getCancellation(), cancel);
		Assert.assertNotNull(data.getCancellationDate());

	}
	
	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.cancelReservation(null);
	}
	
	
	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.cancelReservation("");
	}
	
	
	@Test(expected = ActivityException.class)
	public void whiteReference() {
		ActivityProvider.cancelReservation("    ");
	}
	
	@Test(expected = ActivityException.class)
	public void whiteReference2() {
		ActivityProvider.cancelReservation("    \t \n");
	}
	
	@Test(expected = ActivityException.class)
	public void  ReferenceNotExist(){
		ActivityProvider.cancelReservation("JK1110");
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
