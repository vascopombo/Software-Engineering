package pt.ulisboa.tecnico.softeng.activity.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class ActivityProviderConstructorMethodTest {

	@Test
	public void success() {
		ActivityProvider provider = new ActivityProvider("XtremX", "Adventure++");

		Assert.assertEquals("Adventure++", provider.getName());
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
		Assert.assertEquals(1, ActivityProvider.providers.size());
		Assert.assertEquals(0, provider.getNumberOfActivities());
	}
	
	@Test
	public void codeSizeSuccess(){
		ActivityProvider provider = new ActivityProvider("XPTO99", "Carcavelos");
		Assert.assertTrue(provider.getCode().length() == ActivityProvider.CODE_SIZE);
	}
	
	@Test(expected = ActivityException.class)
	public void checkCodeSpaces(){
		ActivityProvider provider = new ActivityProvider("XPT  9", "Carcavelos");
	}
	
	@Test(expected = ActivityException.class)
	public void codeSizeOver(){
		ActivityProvider provider = new ActivityProvider("XPTO999", "Carcavelos");
	}
	
	@Test(expected = ActivityException.class)
	public void codeSizeUnder(){
		ActivityProvider provider = new ActivityProvider("XPTO9", "Carcavelos");
	}

	@Test
	public void uniqueNameSuccess(){
		ActivityProvider provider = new ActivityProvider("XPTO99", "Carcavelos");
		ActivityProvider provider1 = new ActivityProvider("XPTO17", "Caparica");
	}

	@Test(expected = ActivityException.class)
	public void uniqueName() {
		ActivityProvider provider = new ActivityProvider("XPTO12", "Caparica");
		ActivityProvider provider1 = new ActivityProvider("XPTO17", "Caparica");
	}

	@Test
	public void uniqueCodeSuccess(){
		ActivityProvider provider = new ActivityProvider("XPTO99", "Carcavelos");
		ActivityProvider provider1 = new ActivityProvider("XPTO17", "Caparica");
	}

	@Test(expected = ActivityException.class)
	public void uniqueCode() {
		ActivityProvider provider = new ActivityProvider("XPTO12", "Carcavelos");
		ActivityProvider provider1 = new ActivityProvider("XPTO12", "Caparica");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
