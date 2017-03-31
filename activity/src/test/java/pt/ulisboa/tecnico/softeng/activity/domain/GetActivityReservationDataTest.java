package pt.ulisboa.tecnico.softeng.activity.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;



public class GetActivityReservationDataTest {
	Activity activity;
	ActivityProvider provider;
	ActivityOffer offer;
	Booking booking; 
	


	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		this.activity = new Activity(this.provider, "Bush Walking", 18, 80, 3);

		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		this.offer = new ActivityOffer(activity, begin, end);
		this.booking = new Booking(this.provider, this.offer);
	}
	
	@Test
	public void success() {
		String reference = this.booking.getReference();
		ActivityReservationData data = ActivityProvider.getActivityReservationData(reference);
		
		Assert.assertEquals(this.booking.getReference(), data.getReference());
		Assert.assertEquals(this.provider.getName(), data.getName());
		Assert.assertEquals(this.provider.getCode(), data.getCode());
		Assert.assertEquals(this.offer.getEnd(), data.getEnd());
		Assert.assertEquals(this.offer.getBegin(), data.getBegin());	
	}
	
	@Test(expected = ActivityException.class)
	public void nullReference() {
		ActivityProvider.getActivityReservationData(null);
	}
	
	
	@Test(expected = ActivityException.class)
	public void emptyReference() {
		ActivityProvider.getActivityReservationData("");
	}
	
	
	@Test(expected = ActivityException.class)
	public void blankReference() {
		ActivityProvider.getActivityReservationData("    ");
	}
	
	@Test(expected = ActivityException.class)
	public void tabReference() {
		ActivityProvider.getActivityReservationData("    \t \n");
	}
	
	@Test(expected = ActivityException.class)
	public void  ReferenceNotExist(){
		ActivityProvider.getActivityReservationData("JK1110");
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
