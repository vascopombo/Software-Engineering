package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.StrictExpectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

@RunWith(JMockit.class)
public class ReserveActivityStateProcessMethodTest {
	
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.RESERVE_ACTIVITY);
		}

	
	@Test
	public void FailBookingActivityException(@Mocked final ActivityInterface activityInterface) {

			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin, end, this.anyInt);
					this.result = new ActivityException();
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		}

	
	@Test
	public void FailBookingSecondRemoteException(@Mocked final ActivityInterface activityInterface) {

			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin, end, this.anyInt);
					this.result = new RemoteAccessException();
					this.times = 5;
					
				}
			};

			this.adventure.process();
			this.adventure.process();
			this.adventure.process();
			this.adventure.process();
			this.adventure.process();
			
			Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		}
	
	@Test
	public void SameDate(@Mocked final ActivityInterface activityInterface) {
		 LocalDate end = new LocalDate(2016, 12, 19);
		 LocalDate begin = new LocalDate(2016, 12, 19);
		this.adventure = new Adventure(this.broker, begin, end, 20, IBAN, 300);
		this.adventure.setState(State.RESERVE_ACTIVITY);
		
			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin, end, this.anyInt);
					this.result = this.anyString;
					
				}
			};

			this.adventure.process();
			
			Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
			
		}
	
	@Test
	public void DiferenteDate(@Mocked final ActivityInterface activityInterface) {
		
		
			new StrictExpectations() {
				{
					ActivityInterface.reserveActivity(begin, end, this.anyInt);
					this.result = this.anyString;
					
				}
			};

			this.adventure.process();
			
			Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		}

	}
