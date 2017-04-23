package pt.ulisboa.tecnico.softeng.activity.domain;

import static org.junit.Assert.assertEquals;
import org.joda.time.LocalDate;

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
	private static final String ACTIVITY_NAME = "futebol";
	private static final int MIN_AGE = 18;
	private static final int MAX_AGE = 40;
	private static final int CAPACITY= 22;
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	
	@Test
	public void success() {
	 	atomicProcess();
	 	atomicAssert();
	 }

	@Atomic(mode = TxMode.WRITE)
	 public void atomicProcess() {
		ActivityProvider ap = new ActivityProvider(PROVIDER_CODE, PROVIDER_NAME);
		Activity a = new Activity(ap, ACTIVITY_NAME, MIN_AGE, MAX_AGE, CAPACITY);
		ActivityOffer ao = new ActivityOffer(a, begin, end);
	 }
	
	@Atomic(mode = TxMode.READ)
	 	public void atomicAssert() {	
			assertEquals(1, FenixFramework.getDomainRoot().getActivityproviderSet().size());
		
		 	List<ActivityProvider> activityProvider = new ArrayList<>(FenixFramework.getDomainRoot().getActivityproviderSet());
		 	ActivityProvider activityprovider = activityProvider.get(0);

		 	assertEquals(PROVIDER_CODE, activityprovider.getCode());
		 	assertEquals(PROVIDER_NAME, activityprovider.getName());

		 	assertEquals(1, activityprovider.getActivitySet().size());

			List<Activity> activities = new ArrayList<>(activityprovider.getActivitySet());
			
			Activity activity = activities.get(0);

			assertEquals(activityprovider, activity.getActivityProvider());
			assertEquals(ACTIVITY_NAME, activity.getName());
			assertEquals(MIN_AGE, activity.getMinAge());
			assertEquals(MAX_AGE, activity.getMaxAge());
			assertEquals(CAPACITY, activity.getCapacity());
			
			assertEquals(1, activity.getActivityOfferSet().size());
			
			List<ActivityOffer> activityOffer = new ArrayList<>(activity.getActivityOfferSet());
			ActivityOffer activityoffer = activityOffer.get(0);
			
			assertEquals(activity, activityoffer.getActivity());
			assertEquals(begin,activityoffer.getBegin());
			assertEquals(end, activityoffer.getEnd());
			
		}
	
		@After
		@Atomic(mode = TxMode.WRITE)
		public void tearDown() {
			for (ActivityProvider activityProvider : FenixFramework.getDomainRoot().getActivityproviderSet()) {
				activityProvider.delete();
			}
		}

}
