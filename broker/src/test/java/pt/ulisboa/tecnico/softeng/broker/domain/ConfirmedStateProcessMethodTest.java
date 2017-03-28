package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.StrictExpectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

@RunWith(JMockit.class)
public class ConfirmedStateProcessMethodTest {
	private static final String IBAN = "BK01987654321";
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	int i = 0;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.CONFIRMED);
	}

	@Test
	public void success(@Mocked final BankInterface bankInterface,
					@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		
		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		
		new Verifications() {
			{
				BankInterface.getOperationData(this.anyString);
				this.times = 1;
				
				ActivityInterface.getActivityReservationData(this.anyString);
				this.times = 1;
				
				HotelInterface.getRoomBookingData(this.anyString);
				this.times = 1;
			}
		};
	}
	
	@Test
	public void PaymentFourTimes(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};
		
		for(i=0;i<4;i++){
			this.adventure.process();
		}		

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}	
	
	@Test
	public void PaymentFiveTimesBankException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();
			}
		};
		for(i=0;i<5;i++){
			this.adventure.process();
		}		

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void PaymentNineteenTimesRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for(i=0;i<19;i++){
			this.adventure.process();
		}

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}
	
	@Test
	public void PaymentTwentyTimesRemoteAccessException(@Mocked final BankInterface bankInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);

		new Expectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new RemoteAccessException();
			}
		};

		for(i=0;i<20;i++){
			this.adventure.process();
		}

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void ActivityReceiveActivityException(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

	@Test
	public void ActivitySuccess(@Mocked final BankInterface bankInterface, @Mocked final ActivityInterface activityInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityReservationData();
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
	}

	@Test
	public void HotelExceptionReceived(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);

		new StrictExpectations() {
			{
				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityReservationData();
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();
				
			}
		};

		this.adventure.process();

		Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
	}

}
