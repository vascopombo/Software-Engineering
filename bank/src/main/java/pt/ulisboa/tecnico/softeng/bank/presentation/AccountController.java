package pt.ulisboa.tecnico.softeng.bank.presentation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.bank.services.local.BankInterface;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.AccountData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.BankData.CopyDepth;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData;
import pt.ulisboa.tecnico.softeng.bank.services.local.dataobjects.ClientData.CopyDepth2;

@Controller
@RequestMapping(value = "/banks/{bankCode}/clients/{clientId}/accounts")
public class AccountController {
	private static Logger logger = LoggerFactory.getLogger(AccountController.class);
		
	@RequestMapping(method = RequestMethod.GET)
	public String showAccounts(Model model, @PathVariable String bankCode, @PathVariable String clientId) {
		logger.info("showAccounts bankCode:{} clientId:{}", bankCode, clientId);
		
		BankData bankData = BankInterface.getBankDataByCode(bankCode, CopyDepth.CLIENTS);
		ClientData clientData = BankInterface.getClientDataById(bankCode,clientId, CopyDepth2.ACCOUNTS);
		if (bankData == null || clientData == null) {
			model.addAttribute("error", "Error: it does not exist a bank with the code" + bankCode + "and client with the id " + clientId);
			model.addAttribute("bank", new BankData());
			model.addAttribute("client", new ClientData());
			model.addAttribute("clients", BankInterface.getClients());
			return "clients";
		} else {
			model.addAttribute("account", new AccountData());
			model.addAttribute("bank", bankData);
			model.addAttribute("client", clientData);
			return "accounts";
		}
	}
	
	@RequestMapping(method = RequestMethod.POST)
	public String submission(Model model, @PathVariable String bankCode, @PathVariable String clientId,
			@ModelAttribute AccountData accountData) {
		logger.info("accountSubmit clientId:{}, bankCode:{} account iban:{}, account bank:{}, account balance:{}", clientId, bankCode, accountData.getIBAN(),accountData.getBank(),accountData.getBalance());

		if(accountData.getIBAN()!=null){
			if(BankInterface.getAccountDataByIban(accountData.getIBAN())==null){
				model.addAttribute("error", "Error: Account not found");
				model.addAttribute("account", accountData);
				model.addAttribute("client", BankInterface.getClientDataById(bankCode,clientId, CopyDepth2.ACCOUNTS));
				model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode, CopyDepth.CLIENTS));
				return "accounts";				
			}
			try {		
				BankInterface.createOperation(accountData.getIBAN(), accountData.getBalance());
			}
			catch (BankException be) {
				model.addAttribute("error", "Error: it was not possible to create the operation");
				model.addAttribute("account", accountData);
				model.addAttribute("client", BankInterface.getClientDataById(bankCode,clientId, CopyDepth2.ACCOUNTS));
				model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode, CopyDepth.CLIENTS));
				return "accounts";
			}
		}
		else{
			try {		
				BankInterface.createAccount(bankCode, clientId);
			} catch (BankException be) {
				model.addAttribute("error", "Error: it was not possible to create the account");
				model.addAttribute("account", accountData);
				model.addAttribute("client", BankInterface.getClientDataById(bankCode,clientId, CopyDepth2.ACCOUNTS));
				model.addAttribute("bank", BankInterface.getBankDataByCode(bankCode, CopyDepth.CLIENTS));
				return "accounts";
			}
		}

		return "redirect:/banks/" + bankCode + "/clients/" + clientId + "/accounts";
	}
	
}