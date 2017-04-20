package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Client extends Client_Base{
	private static int counter = 0;

	public Client(Bank bank, String name) {
		checkArguments(bank, name);
		
		setID(Integer.toString(++Client.counter));
		setName(name);
		setBank(bank);
		bank.addClient(this);
	}
	
	public void delete() {
		setBank(null);

	for(Account account : getAccountSet()){
 		account.delete();
 	}
		deleteDomainObject();
	}

	private void checkArguments(Bank bank, String name) {
		if (bank == null || name == null || name.trim().equals("")) {
			throw new BankException();
		}
	}

}
