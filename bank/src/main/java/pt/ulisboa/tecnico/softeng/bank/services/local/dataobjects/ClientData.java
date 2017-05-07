package pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects;

import java.util.ArrayList;
import java.util.List;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;

public class ClientData {
 
 public static enum CopyDepth2 {
	 ACCOUNTS, SHALLOW
 };
 
 private String name;
 private Bank bank;
 private String id;
 
 private List<AccountData> accounts = new ArrayList<>();
 
 public ClientData() {
 }
 
 public ClientData(Client client, CopyDepth2 depth) {
  this.name = client.getName();
  this.bank = client.getBank();
  this.id = client.getID();
  
  switch (depth) {
  case SHALLOW:
	   break;
  case ACCOUNTS:
   for (Account account : client.getAccountSet()) {
    this.accounts.add(new AccountData(account));
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
 
 public Bank getBank() {
  return this.bank;
 }
 
 public void setBank(Bank bank) {
  this.bank = bank;
 }

 public String getId() {
  return this.id;
 }

 public void setId(String id) {
  this.id = id;
 }

 public List<AccountData> getAccounts() {
  return this.accounts;
 }

 public void setAccounts(List<AccountData> accounts) {
  this.accounts = accounts;
 }
 
}
