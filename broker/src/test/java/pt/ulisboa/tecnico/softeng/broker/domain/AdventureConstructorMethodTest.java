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
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;

public class AdventureConstructorMethodTest {
	private Broker broker;
	private Bank bank;
	private Account account;
	private Client client;

	@Before
	public void setUp() {
		this.broker = new Broker("BR01", "eXtremeADVENTURE");
		this.bank = new Bank("Money", "BK99");
		this.client = new Client(bank, "Diogo");
		this.account = new Account(bank, client);
	}

	@Test
	public void success() {
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);

		Adventure adventure = new Adventure(this.broker, begin, end, 20, "BK011234567", 300);

		Assert.assertEquals(this.broker, adventure.getBroker());
		Assert.assertEquals(begin, adventure.getBegin());
		Assert.assertEquals(end, adventure.getEnd());
		Assert.assertEquals(20, adventure.getAge());
		Assert.assertEquals("BK011234567", adventure.getIBAN());
		Assert.assertEquals(300, adventure.getAmount());
		Assert.assertTrue(this.broker.hasAdventure(adventure));

		Assert.assertNull(adventure.getBankPayment());
		Assert.assertNull(adventure.getActivityBooking());
		Assert.assertNull(adventure.getRoomBooking());
	}

	@Test(expected = BrokerException.class)
	public void negativeAmount(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, this.account.getIBAN(), -10);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void nullIBAN(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, null, 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void emptyIBAN(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 20, "", 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void wrongDate(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, end, begin, 20, this.account.getIBAN(), 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void nullBeginDate(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, null, end, 20, this.account.getIBAN(), 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void nullEndDate(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, null, 20, this.account.getIBAN(), 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void negativeAge(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, -20, this.account.getIBAN(), 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}

	@Test(expected = BrokerException.class)
	public void excessiveAge(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, begin, end, 200, this.account.getIBAN(), 300);
		try{
			adventure.process();
		}catch(BankException be){
			throw new BrokerException();
		}
	}
	
	@Test(expected = BrokerException.class)
	public void nullBroker(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(null, begin, end, 20, this.account.getIBAN(), 300);
	}
	
	@Test(expected = BrokerException.class)
	public void nullBegin(){
		LocalDate end = new LocalDate(2016, 12, 21);
		Adventure adventure = new Adventure(this.broker, null, end, 20, this.account.getIBAN(), 300);
	}
	
	@Test(expected = BrokerException.class)
	public void nullEnd(){
		LocalDate begin = new LocalDate(2016, 12, 19);
		Adventure adventure = new Adventure(this.broker, begin, null, 20, this.account.getIBAN(), 300);
	}
	
	@After
	public void tearDown() {
		Broker.brokers.clear();
		Bank.banks.clear();
	}

}
