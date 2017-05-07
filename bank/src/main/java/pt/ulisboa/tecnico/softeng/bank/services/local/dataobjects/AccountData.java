package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class AccountData {

	private Bank bank;
	private Client client;
	private String IBAN;
	private int balance;
	

	public AccountData() {
	}

	public AccountData(Account account) {
		this.bank = account.getBank();
		this.client = account.getClient();
		this.IBAN = account.getIBAN();
		this.balance = account.getBalance();
	}

	public Bank getBank() {
		return this.bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
	public Client getClient() {
		return this.client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	public String getIBAN() {
		return this.IBAN;
	}
	
	public void setIBAN(String iban) {
		this.IBAN = iban;
	}

	public int getBalance() {
		return this.balance;
	}
	
	public void setBalance(int balance) {
		this.balance = balance;
	}


}
