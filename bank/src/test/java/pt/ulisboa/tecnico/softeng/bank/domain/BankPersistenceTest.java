package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;
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
		new Operation(Type.DEPOSIT, account1, 1000);
		new Operation(Type.WITHDRAW, account1, 500);
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
		
		assertEquals(1, bank.getClientSet().size());
		List<Client> clients = new ArrayList<>(bank.getClientSet());
		Client client = clients.get(0);
		List<Account> clientAccounts = new ArrayList<>(client.getAccountSet());
		assertEquals(1, clientAccounts.size());
		Account account1 = clientAccounts.get(0);
		
		assertNotNull(client.getID());
		assertEquals(CLIENT_NAME, client.getName());
		assertEquals(BANK_NAME, client.getBank().getName());
		assertEquals(BANK_CODE, client.getBank().getCode());
		assertNotNull(account1.getIBAN());
		assertEquals(0, account1.getBalance());
		assertEquals(BANK_NAME, account1.getBank().getName());
		assertEquals(BANK_CODE, account1.getBank().getCode());
		assertEquals(CLIENT_NAME, account1.getClient().getName());
		
		assertEquals(2, bank.getOperationSet().size());
		List<Operation> op = new ArrayList<>(bank.getOperationSet());
		Operation operation1 = op.get(0);
		Operation operation2 = op.get(1);
		
		assertEquals(Type.DEPOSIT, operation1.getType());
		assertEquals(1000, operation1.getValue());
		assertEquals(account1, operation1.getAccount());
		assertTrue(operation1.getTime() != null);
		assertEquals(operation1, bank.getOperation(operation1.getReference()));
		
		assertEquals(Type.WITHDRAW, operation2.getType());
		assertEquals(500, operation2.getValue());
		assertEquals(account1, operation2.getAccount());
		assertTrue(operation2.getTime() != null);
		assertEquals(operation2, bank.getOperation(operation2.getReference()));
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}
