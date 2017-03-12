package pt.ulisboa.tecnico.softeng.bank.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import java.util.regex.Pattern;

public class Bank {
	public static Set<Bank> banks = new HashSet<>();

	public static final int CODE_SIZE = 4;

	private final String name;
	private final String code;
	private final Set<Account> accounts = new HashSet<>();
	private final Set<Client> clients = new HashSet<>();
	private final List<Operation> log = new ArrayList<>();

	public Bank(String name, String code) {
		checkArg(name,code);
		checkCode(code);
		checkUniqueCode(code);

		this.name = name;
		this.code = code;

		Bank.banks.add(this);
	}

	private void checkCode(String code) {
		if (code.length() != Bank.CODE_SIZE) {
			throw new BankException();
		}
	}

	private void checkUniqueCode(String code){
		for (Bank bank: banks){
			if (code==bank.getCode()){
				throw new BankException();
			}
		}
	}
	
	String getName() {
		return this.name;
	}

	String getCode() {
		return this.code;
	}

	int getNumberOfAccounts() {
		return this.accounts.size();
	}

	int getNumberOfClients() {
		return this.clients.size();
	}

	void addAccount(Account account) {
		this.accounts.add(account);
	}

	boolean hasClient(Client client) {
		return this.clients.contains(client);
	}

	void addClient(Client client) {
		this.clients.add(client);
	}

	void addLog(Operation operation) {
		this.log.add(operation);
	}

	public Account getAccount(String IBAN) {
		for (Account account : this.accounts) {
			if (account.getIBAN().equals(IBAN)) {
				return account;
			}
		}
		throw new BankException();
	}

	public Operation getOperation(String reference) {
		for (Operation operation : this.log) {
			if (operation.getReference().equals(reference)) {
				return operation;
			}
		}
		return null;
	}

	public static String processPayment(String IBAN, int amount) {
		for (Bank bank : Bank.banks) {
			if (bank.getAccount(IBAN) != null) {
				return bank.getAccount(IBAN).withdraw(amount);
			}
		}
		return null;
	}
	
	public void checkArg(String name, String code){
		if (code==null || name==null){
			throw new BankException();
		}
		else if(name.isEmpty() || code.isEmpty()){
			throw new BankException();
		}
		else if(name.replaceAll("\\s+","").trim().isEmpty() ||
				code.replaceAll("\\s+","").trim().isEmpty()){
			throw new BankException();
		}
	}

}
