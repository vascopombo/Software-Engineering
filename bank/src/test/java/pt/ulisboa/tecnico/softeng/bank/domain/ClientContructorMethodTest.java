package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class ClientContructorMethodTest {
	Bank bank;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
	}

	@Test
	public void success() {
		Client client = new Client(this.bank, "António");

		Assert.assertEquals("António", client.getName());
		Assert.assertTrue(client.getID().length() >= 1);
		Assert.assertTrue(this.bank.hasClient(client));
	}
	
	@Test(expected = BankException.class)
	public void nullArg() {
		Client client = new Client(null,null);
	}
	
	@Test(expected = BankException.class)
	public void nullArg1() {
		Client client = new Client(this.bank,null);
	}
	
	@Test(expected = BankException.class)
	public void nullArg2() {
		Client client = new Client(null,"Joao");
	}
	
	@Test(expected = BankException.class)
	public void EmptyArg() {
		Client client = new Client(this.bank,"");
	}

	@Test(expected = BankException.class)
	public void WhiteArg1() {
		Client client = new Client(this.bank,"      ");
	}
	
	
	@Test(expected = BankException.class)
	public void WhiteArg2() {
		Client client = new Client(this.bank,"		\t");
	}
	
	@Test(expected = BankException.class)
	public void WhiteArg3() {
		Client client = new Client(this.bank,"		\n");
	}
	
	@Test(expected = BankException.class)
	public void WhiteArg4() {
		Client client = new Client(this.bank,"		\r");
	}
	
	@Test(expected = BankException.class)
	public void WhiteArg5() {
		Client client = new Client(this.bank,"   	\t  \r	\n");
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
