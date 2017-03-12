package pt.ulisboa.tecnico.softeng.bank.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class BankConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Bank bank = new Bank("Money", "BK01");

		Assert.assertEquals("Money", bank.getName());
		Assert.assertEquals("BK01", bank.getCode());
		Assert.assertEquals(1, Bank.banks.size());
		Assert.assertEquals(0, bank.getNumberOfAccounts());
		Assert.assertEquals(0, bank.getNumberOfClients());
	}

	@Test(expected = BankException.class)
	public void nullArg() {
		Bank bank = new Bank(null,null);
	}
	
	@Test(expected = BankException.class)
	public void nullArg1() {
		Bank bank = new Bank("Joao",null);
	}
	
	@Test(expected = BankException.class)
	public void nullArg2() {
		Bank bank = new Bank(null,"BK02");
	}
	
	@Test(expected = BankException.class)
	public void EmptyArg() {
		Bank bank = new Bank("","");
	}
	
	@Test(expected = BankException.class)
	public void EmptyArg2() {
		Bank bank = new Bank("Joao","");
	}
	
	@Test(expected = BankException.class)
	public void EmptyArg3() {
		Bank bank = new Bank("","BK02");
	}

	@Test(expected = BankException.class)
	public void WhiteArg1() {
		Bank bank = new Bank("\t    \n \r","\t    \n \r");
	}
	
	
	@Test(expected = BankException.class)
	public void WhiteArg2() {
		Bank bank = new Bank("Joao","\t    \n \r");
	}
	
	@Test(expected = BankException.class)
	public void WhiteArg3() {
		Bank bank = new Bank("\t    \n \r","BK02");
	}
	
	@Test(expected = BankException.class)
	public void LengthCode() {
		Bank bank = new Bank("Joao","BK0012");
	}
	
	@Test(expected = BankException.class)
	public void LengthCode2() {
		Bank bank = new Bank("Joao","BK");
	}
	
	@Test(expected = BankException.class)
	public void UniqueCode() {
		Bank bank1 = new Bank("Joao","BK11");
		Bank bank2 = new Bank("Ze","BK11");
	}
	
	@Test(expected = BankException.class)
	public void UniqueCode2() {
		Bank bank1 = new Bank("Joao","BK11");
		Bank bank2 = new Bank("Jose","BK12");
		Bank bank3 = new Bank("Miguel","Bk13");
		Bank bank4 = new Bank("Pedro","BK11");
	}
	
	@After
	public void tearDown() {
		Bank.banks.clear();
	}
}
