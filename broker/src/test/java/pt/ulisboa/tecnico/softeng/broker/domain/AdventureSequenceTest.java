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
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;

@RunWith(JMockit.class)
public class AdventureSequenceTest {
	private static final String IBAN = "BK01987654321";
	private static final int AMOUNT = 300;
	private static final String PAYMENT_CONFIRMATION = "PaymentConfirmation";
	private static final String PAYMENT_CANCELLATION = "PaymentCancellation";
	private static final String ACTIVITY_CONFIRMATION = "ActivityConfirmation";
	private static final String ACTIVITY_CANCELLATION = "ActivityCancellation";
	private static final String ROOM_CONFIRMATION = "RoomConfirmation";
	private static final String ROOM_CANCELLATION = "RoomCancellation";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private Adventure adventure;
	private Adventure adventure_equalsDates;
	private AdventureState state;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}

	@Test
	public void SequenceCancelledOne(@Mocked final BankInterface bankInterface) {
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new BankException();
			}
		};
		this.adventure.process();

		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void SequenceCancelledTwo(@Mocked final BankInterface bankInterface) {

		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				this.result = new RemoteAccessException();
			}
		};
		
		for( int i=0; i<3; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void SequenceCancelledThree(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN, AMOUNT);
				
				ActivityInterface.reserveActivity(begin, end, 20);
				this.result = new ActivityException();

			}
		};

		for( int i=0; i<3; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}

	@Test
	public void SequenceCancelledFour(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);
				
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
				this.result = new HotelException();

			}
		};

		for( int i=0; i<4; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void SequenceCancelledFive(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new Expectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);

				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
				this.result = new RemoteAccessException();

			}
		};

		for( int i=0; i<13; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void SequenceCancelledSix(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		LocalDate end = new LocalDate(2016, 12, 30);
		LocalDate begin = new LocalDate(2016, 12, 30);
		this.adventure = new Adventure(this.broker, begin, end, 20, IBAN, 300);
		this.adventure.setState(State.PROCESS_PAYMENT);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankException();

			}
		};

		for( int i=0; i<8; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void SequenceCancelledSeven(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure_equalsDates.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure_equalsDates.setRoomCancellation(ROOM_CANCELLATION);
		this.adventure_equalsDates.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure_equalsDates.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityException();
			}
		};

		for( int i=0; i<6; i++){
			this.adventure_equalsDates.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure_equalsDates.getState());
	}
	
	@Test
	public void SequenceCancelledEight(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);
				
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);

				BankInterface.getOperationData(adventure.getPaymentConfirmation());
				this.result = new BankOperationData();
				
				ActivityInterface.getActivityReservationData(adventure.getActivityConfirmation());
				this.result = new ActivityException();

			}
		};

		for( int i=0; i<6; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void SequenceCancelledNine(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);
				
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);

				BankInterface.getOperationData(adventure.getPaymentConfirmation());
				this.result = new BankException();

			}
		};

		for( int i=0; i<9; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
	
	@Test
	public void SequenceCancelledTen(@Mocked final BankInterface bankInterface,
			@Mocked final ActivityInterface activityInterface, @Mocked final HotelInterface hotelInterface) {
		
		this.adventure.setRoomConfirmation(ROOM_CONFIRMATION);
		this.adventure.setPaymentConfirmation(PAYMENT_CONFIRMATION);
		this.adventure.setActivityConfirmation(ACTIVITY_CONFIRMATION);
		this.adventure.setPaymentCancellation(PAYMENT_CANCELLATION);
		this.adventure.setActivityCancellation(ACTIVITY_CANCELLATION);
		this.adventure.setRoomCancellation(ROOM_CANCELLATION);
		
		new StrictExpectations() {
			{
				BankInterface.processPayment(IBAN,AMOUNT);

				ActivityInterface.reserveActivity(begin, end, 20);
				
				HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);

				BankInterface.getOperationData(PAYMENT_CONFIRMATION);
				this.result = new BankOperationData();
				
				ActivityInterface.getActivityReservationData(ACTIVITY_CONFIRMATION);
				this.result = new ActivityReservationData();
				
				HotelInterface.getRoomBookingData(ROOM_CONFIRMATION);
				this.result = new HotelException();

			}
		};

		for( int i=0; i<7; i++){
			this.adventure.process();
		}
		
		Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
	}
}
