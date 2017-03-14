package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityConstructorMethodTest {
	private ActivityProvider provider;

	@Before
	public void setUp() {
		this.provider = new ActivityProvider("XtremX", "ExtremeAdventure");
	}

	@Test
	public void success() {
		Activity activity = new Activity(this.provider, "Bush Walking", 18, 80, 25);

		Assert.assertTrue(activity.getCode().startsWith(this.provider.getCode()));
		Assert.assertTrue(activity.getCode().length() > ActivityProvider.CODE_SIZE);
		Assert.assertEquals("Bush Walking", activity.getName());
		Assert.assertEquals(18, activity.getMinAge());
		Assert.assertEquals(80, activity.getMaxAge());
		Assert.assertEquals(25, activity.getCapacity());
		Assert.assertEquals(0, activity.getNumberOfOffers());
		Assert.assertEquals(1, this.provider.getNumberOfActivities());
	}
	
	@Test
	public void equalAgeSuccess(){
		Activity activity = new Activity(this.provider, "BungeeJumping", 99, 99, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void emptyArg(){
		Activity activity = new Activity(this.provider, "", 18, 80, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void nullArg(){
		Activity activity = new Activity(this.provider, null, 18, 80, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void whitespaceArg(){
		Activity activity = new Activity(this.provider, "     ", 18, 80, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void testMinAge(){
		Activity activity = new Activity(this.provider, "Climbing", 17, 80, 25);
		Activity activity1 = new Activity(this.provider, "Hiking", -100, 80, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void testMaxAge(){
		Activity activity = new Activity(this.provider, "SkyDiving", 18, 100, 25);
		Activity activity1 = new Activity(this.provider, "Rafting", 18, 9999, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void testAges(){
		Activity activity = new Activity(this.provider, "Rapel", 81, 80, 25);
		Activity activity1 = new Activity(this.provider, "Rapel", 99, 18, 25);
	}
	
	@Test(expected = ActivityException.class)
	public void testCapacity(){
		Activity activity = new Activity(this.provider, "Shooting", 18, 80, 0);
		Activity activity1 = new Activity(this.provider, "Boxing", 18, 80, -200);
	}
	
	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
