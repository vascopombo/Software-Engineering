package pt.ulisboa.tecnico.softeng.bank.domain;

import org.joda.time.LocalDateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankGetOperationData {
	
	Bank bank;
	Client client;
	Operation op;
	Account account;
	

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
		this.account = new Account(this.bank, this.client);
		this.op = new Operation(Type.DEPOSIT, this.account, 50);
		
	}

	@Test
	public void success() {
		

		String reference = this.op.getReference();
		BankOperationData data = Bank.getOperationData(reference);

		Assert.assertEquals("DEPOSIT", data.getType());
		Assert.assertEquals(this.op.getValue(), data.getValue());
		Assert.assertEquals(this.account.getIBAN(), data.getIban());
		Assert.assertEquals(this.op.getReference(), data.getReference());
		Assert.assertEquals(this.op.getTime(), data.getType());
	}
	
	@Test
	public void SetTest() {
		
		String reference = this.op.getReference();
		BankOperationData data = Bank.getOperationData(reference);
		
		data.setIban("BK0128");
		data.setReference("BK01192");
		data.setType("WITHDRAW");
		data.setValue(20);
		data.setTime(LocalDateTime.now());

		Assert.assertEquals("WITHDRAW", data.getType());
		Assert.assertEquals(20, data.getValue());
		Assert.assertEquals("BK0128", data.getIban());
		Assert.assertEquals("BK01192", data.getReference());
		Assert.assertEquals(LocalDateTime.now(), data.getType());
	}
	
	

	@Test(expected = BankException.class)
	public void nullReference() {
		Bank.getOperationData(null);
	}

	@Test(expected = BankException.class)
	public void emptyReference() {
		Bank.getOperationData("");
	}

	@Test(expected = BankException.class)
	public void blankReference() {
		Bank.getOperationData("    ");
	}

	@Test(expected = BankException.class)
	public void ReferenceDoNoMatch() {
		Bank.getOperationData("RB02");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}


}
