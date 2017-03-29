package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Injectable;
import mockit.Mocked;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

@RunWith(JMockit.class)
public class ProcessPaymentStateProcessMethodTest {
	
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	private final int amount = 300;
	
	private Adventure adventure;

	@Injectable
	private Broker broker;
	
	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, amount);
		this.adventure.setState(State.PROCESS_PAYMENT);
	}
	
	@Test
	public void ProcessPaymentSucceeded(@Mocked final BankInterface bankInterface) {

			new StrictExpectations() {
				{
					BankInterface.processPayment(IBAN, amount);
					this.times = 1;
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.RESERVE_ACTIVITY, this.adventure.getState());
		}
	
	@Test
	public void ProcessPaymentFailedFirstException(@Mocked final BankInterface bankInterface) {

			new StrictExpectations() {
				{
					BankInterface.processPayment(IBAN, amount);
					this.result = new BankException();
					this.times = 1;
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
		}
	
	@Test
	public void ProcessPaymentFailedSecondException(@Mocked final BankInterface bankInterface) {

			new StrictExpectations() {
				{
					BankInterface.processPayment(IBAN, amount);
					this.result = new RemoteAccessException();
					this.times = 3;	
				}
			};

			this.adventure.process();
			this.adventure.process();
			this.adventure.process();
			
			Assert.assertEquals(Adventure.State.CANCELLED, this.adventure.getState());
		}
}