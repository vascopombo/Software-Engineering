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

	@Test
	public void uniqueNameAndCodeSuccess(){
		ActivityProvider provider = new ActivityProvider("XPTO99", "Carcavelos");
		ActivityProvider provider1 = new ActivityProvider("XPTO17", "Caparica");
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
	
	@Test(expected = ActivityException.class)
	public void uniqueName() {
		ActivityProvider provider = new ActivityProvider("XPTO12", "Caparica");
		ActivityProvider provider1 = new ActivityProvider("XPTO17", "Caparica");
	}

	@Test(expected = ActivityException.class)
	public void uniqueCode() {
		ActivityProvider provider = new ActivityProvider("XPTO12", "Carcavelos");
		ActivityProvider provider1 = new ActivityProvider("XPTO12", "Caparica");
	}

	@Test(expected = ActivityException.class)
	public void nullArgs() {
		ActivityProvider provider = new ActivityProvider(null,null);
	}

	@Test(expected = ActivityException.class)
	public void nullArg1() {
		ActivityProvider provider = new ActivityProvider(null,"Carcavelos");
	} 

	@Test(expected = ActivityException.class)
	public void nullArg2() {
		ActivityProvider provider = new ActivityProvider("XPTO12",null);
	}

	@Test(expected = ActivityException.class)
	public void EmptyArgs() {
		ActivityProvider provider = new ActivityProvider("","");
	}

	@Test(expected = ActivityException.class)
	public void EmptyArg1() {
		ActivityProvider provider = new ActivityProvider("","Caparica");
	}

	@Test(expected = ActivityException.class)
	public void EmptyArg2() {
		ActivityProvider provider = new ActivityProvider("XPTO17","");
	}

	@Test(expected = ActivityException.class)
	public void whitespaceArgs() {
		ActivityProvider provider = new ActivityProvider("      ","      ");
	}

	@Test(expected = ActivityException.class)
	public void whitespaceArg1() {
		ActivityProvider provider = new ActivityProvider("      ","Carcavelos");
	}

	@Test(expected = ActivityException.class)
	public void whitespaceArg2() {
		ActivityProvider provider = new ActivityProvider("XPTO99","       ");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
