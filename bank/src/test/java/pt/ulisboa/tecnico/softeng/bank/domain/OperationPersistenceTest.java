package pt.ulisboa.tecnico.softeng.bank.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation.Type;

public class OperationPersistenceTest {
	private static final String CLIENT_NAME = "Torpedo";
	private static final String BANK_NAME = "Money";
	private static final String BANK_CODE = "BK00";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Bank bank = new Bank(BANK_NAME, BANK_CODE);
		Client client = new Client(bank, CLIENT_NAME);
		Account account = new Account(bank, client);
		new Operation(Type.DEPOSIT, account, 1000);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		Bank bank = Bank.getBankByCode(BANK_CODE);
		
		Client client = null;
		for(Client c : bank.getClientSet()) {
			
			if(c.getName().equals(CLIENT_NAME)) {
				client = c;
				break;
			}
		}
		
		Account account = null;
		for(Account a : bank.getAccountSet()) {
			
			if(a.getClient().equals(client)) {
				account = a;
				break;
			}
			
		Operation operation = null;
		for(Operation op : bank.getOperationSet()) {
			
			if(op.getAccount().equals(account)) {
				operation = op;
				break;
			}
		}
		Assert.assertTrue(operation.getReference().startsWith(bank.getCode()));
		Assert.assertEquals(Type.DEPOSIT, operation.getType());
		Assert.assertEquals(1000, operation.getValue());
		Assert.assertEquals(account, operation.getAccount());
		Assert.assertTrue(operation.getTime() != null);
		Assert.assertEquals(operation, bank.getOperation(operation.getReference()));
		}	
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Bank bank : FenixFramework.getDomainRoot().getBankSet()) {
			bank.delete();
		}
	}

}