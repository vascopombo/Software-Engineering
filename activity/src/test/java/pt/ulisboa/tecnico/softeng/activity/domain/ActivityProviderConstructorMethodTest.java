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

	@Test(expected = ActivityException.class)
	public void uniqueName() {
		ActivityProvider provider = new ActivityProvider("XPTO12", "Sara");
		ActivityProvider provider1 = new ActivityProvider("XPTO17", "Sara");
	}

	@Test(expected = ActivityException.class)
	public void uniqueCode() {
		ActivityProvider provider = new ActivityProvider("XPTO12", "Pedro");
		ActivityProvider provider1 = new ActivityProvider("XPTO12", "Amilcar");
	}

	@After
	public void tearDown() {
		ActivityProvider.providers.clear();
	}

}
