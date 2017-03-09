package pt.ulisboa.tecnico.softeng.bank.domain;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import java.util.regex.Pattern;

public class Client {
	private static int counter = 0;

	private final String name;
	private final String ID;

	public Client(Bank bank, String name) {
		verifyClientArg(bank,name);
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
	
	public void verifyClientArg( Bank bank, String name){
		if (bank==null || name==null){
			throw new BankException();
		}
		else if(name.isEmpty()){
			throw new BankException();
		}
		else if(name.replaceAll("\\+s","").trim().isEmpty()){
			throw new BankException();
		}
	}

}
