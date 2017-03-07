package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;

public class Client {
	private static int counter = 0;

	private final String name;
	private final String ID;

	public Client(Bank bank, String name) {
		verifica(bank,name);
		this.ID = Integer.toString(++Client.counter);
		this.name = name;

		bank.addClient(this);
	}

	public String getName() {
		return this.name;
	}

	public String getID() {
		return this.ID;
	}
	
	public void verifica( Bank bank, String name){
		if (bank==null || name==null){
			throw new BankException();
		}
		else if(name==""){
			throw new BankException();
		}
		else if(name.trim().isEmpty()){
			throw new BankException();
		}
	}

}
