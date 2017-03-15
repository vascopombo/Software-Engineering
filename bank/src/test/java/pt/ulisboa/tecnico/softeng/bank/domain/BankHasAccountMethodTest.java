package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankHasAccountMethodTest {
	Bank bank;
	Client client;

	@Before
	public void setUp() {
		this.bank = new Bank("Money", "BK01");
		this.client = new Client(this.bank, "António");
	}

	@Test
	public void success() {
		Account account = new Account(this.bank, this.client);

		Account result = this.bank.getAccount(account.getIBAN());

		Assert.assertEquals(account, result);
	}

	@Test(expected = BankException.class)
	public void nullIBAN(){
		Account account = new Account(this.bank, this.client);
		Account result = this.bank.getAccount(null);
	}

	@Test(expected = BankException.class)
	public void noAccounts(){
		Account result = this.bank.getAccount("TEST_IBAN");
	}

	@Test(expected = BankException.class)
	public void wrongIBAN(){
		Client c1 = new Client(this.bank, "João");
		Client c2 = new Client(this.bank, "Miguel");
		Client c3 = new Client(this.bank, "Diogo");

		Account acc1 = new Account(this.bank, this.client);
		Account acc2 = new Account(this.bank, c1);
		Account acc3 = new Account(this.bank, c2);
		Account acc4 = new Account(this.bank, c3);

		Account result = this.bank.getAccount("TEST_IBAN");
	}

	@Test(expected = BankException.class)
	public void emptyIBAN() {
		Account = new Account(this.bank, this.client);
		Account result = this.bank.getAccount("");
	}

	@After
	public void tearDown() {
		Bank.banks.clear();
	}

}
