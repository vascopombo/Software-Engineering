package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class ActivityProviderPersistenceTest {

	private static final String PROVIDER_CODE = "test12";
	private static final String PROVIDER_NAME = "badoo";
	
	@Test
	public void success() {
	 	atomicProcess();
	 	atomicAssert();
	 }

	@Atomic(mode = TxMode.WRITE)
	 public void atomicProcess() {
		new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);
	 }
	
	@Atomic(mode = TxMode.READ)
	 	public void atomicAssert() {	
			assertEquals(1, FenixFramework.getDomainRoot().getActivityproviderSet().size());
		
		 	List<ActivityProvider> activityProvider = new ArrayList<>(FenixFramework.getDomainRoot().getActivityproviderSet());
		 	ActivityProvider activityprovider = activityProvider.get(0);

		 	assertEquals(PROVIDER_CODE, activityprovider.getCode());
		 	assertEquals(PROVIDER_NAME, activityprovider.getName());
		
		}
	
		@After
		@Atomic(mode = TxMode.WRITE)
		public void tearDown() {
			for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityproviderSet()) {
				activityProvider.delete();
			}
		}

}
