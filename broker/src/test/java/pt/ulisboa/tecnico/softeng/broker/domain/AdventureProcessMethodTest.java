package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.activity.domain.Activity;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureProcessMethodTest {
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 19);
	private final LocalDate begin2 = new LocalDate(2016, 12, 29);
	private Broker broker;
	private String IBAN;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");

		Bank bank = new Bank("Money", "BK01");
		Client client = new Client(bank, "António");
		Account account = new Account(bank, client);
		this.IBAN = account.getIBAN();
		account.deposit(1000);

		Hotel hotel = new Hotel("XPTO123", "Paris");
		new Room(hotel, "01", Type.SINGLE);

		ActivityProvider provider = new ActivityProvider("XtremX", "ExtremeAdventure");
		Activity activity = new Activity(provider, "Bush Walking", 18, 80, 3);
		new ActivityOffer(activity, this.begin, this.end);
	}

	@Test
	public void success() {
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 300);

		adventure.process();

		Assert.assertNotNull(adventure.getBankPayment());
		Assert.assertNotNull(adventure.getRoomBooking());
		Assert.assertNotNull(adventure.getActivityBooking());
	}

	@Test(expected = BrokerException.class)
	public void AmountTest(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 3000);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void AmountTest2(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, -345);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void AmountTest3(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 20, this.IBAN, 0);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void AgeTest(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 15, this.IBAN, 300);
		adventure.process();
	}

	
	@Test(expected = BrokerException.class)
	public void DateTest(){
		Adventure adventure = new Adventure(this.broker, this.begin2, this.end, 20, this.IBAN, 300);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void AgeTest2(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 200, this.IBAN, 300);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void IBANTest(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 15, "AAABBB", 300);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void IBANTest2(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 15, null, 300);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void IBANTest3(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 15, "", 300);
		adventure.process();
	}
	
	@Test(expected = BrokerException.class)
	public void IBANTest4(){
		Adventure adventure = new Adventure(this.broker, this.begin, this.end, 15, "   \t \n", 300);
		adventure.process();
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
		Hotel.hotels.clear();
		ActivityProvider.providers.clear();
		Broker.brokers.clear();
	}
}
