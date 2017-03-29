package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankCancelPaymentMethodTest {
	
	Bank bank;
	Client client;
	Operation op;
	Account account;
	

	@Before
	public void setUp() {
		this.bank = new Bank("Espirito Falido", "EF01");
		this.client = new Client(this.bank, "Madalena");
		this.account = new Account(this.bank, this.client);
		this.op = new Operation(Type.WITHDRAW, this.account, 400);
		
	}

	@Test
	public void success() {
		

		String reference = this.op.getReference();
		String cancel = Bank.cancelPayment(reference);
		Operation operation = this.bank.getOperation(cancel);

		Assert.assertEquals("DEPOSIT", operation.getType().toString());
		Assert.assertEquals(this.op.getValue(), operation.getValue());
		Assert.assertEquals(this.op.getAccount(), operation.getAccount());
	}
	
	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.cancelPayment(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.cancelPayment("");
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.cancelPayment("    ");
	}

	@Test(expected = BankException.class)
	public void ReferenceDoNoMatch() {
		Bank.cancelPayment("SL25");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}


}
