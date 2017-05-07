package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;


import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.bank.domain.Operation;

public class BankData {
	public static enum CopyDepth {
		SHALLOW, CLIENTS, OPERATIONS
	};

	private String name;
	private String code;
	
	
	private List<BankOperationData> operations = new ArrayList<>();
	private List<ClientData> clients = new ArrayList<>();

	public BankData() {
	}

	public BankData(Bank bank, CopyDepth depth) {
		this.name = bank.getName();
		this.code = bank.getCode();


		switch (depth) {
		case SHALLOW:
			break;		
		case OPERATIONS:
			for (Operation operation : bank.getOperationSet()) {
				this.operations.add(new BankOperationData(operation));
			}
			break;
		case CLIENTS:
			   for (Client client : bank.getClientSet()) {
			    this.clients.add(new ClientData(client, ClientData.CopyDepth2.ACCOUNTS));
			   }
			   break;
		default:
			break;
		}
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	public List<BankOperationData> getOperations() {
		return this.operations;
	}

	public void setOperations(List<BankOperationData> operations) {
		this.operations = operations;
	}
	public List<ClientData> getClients() {
		return this.clients;
	}

	public void setClients(List<ClientData> clients) {
		this.clients = clients;
	}
	
}
