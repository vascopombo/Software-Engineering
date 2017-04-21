package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class BankPersistenceTest {
	private static final String BANK_NAME = "Money";
	private static final String BANK_CODE = "BK01";
	private static final String CLIENT_NAME = "Joana";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank novo = new Bank(BANK_NAME, BANK_CODE);
		Client client1 = new Client(novo, CLIENT_NAME);
		Account account1 = new Account(novo, client1);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		assertEquals(1, FenixFramework.getDomainRoot().getBankSet().size());

		List<Bank> banks = new ArrayList<>(FenixFramework.getDomainRoot().getBankSet());
		Bank bank = banks.get(0);

		assertEquals(BANK_NAME, bank.getName());
		assertEquals(BANK_CODE, bank.getCode());

		assertEquals(1, bank.getAccountSet().size());
		List<Account> accounts = new ArrayList<>(bank.getAccountSet());		
		Account account = accounts.get(0);

		assertNotNull(account.getIBAN());
		assertEquals(0, account.getBalance());
		assertEquals(BANK_NAME, account.getBank().getName());
		assertEquals(BANK_CODE, account.getBank().getCode());
		assertEquals(CLIENT_NAME, account.getClient().getName());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
