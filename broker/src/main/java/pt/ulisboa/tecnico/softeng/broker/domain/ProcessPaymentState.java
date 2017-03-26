package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;

public class ProcessPaymentState extends AdventureState{
	private static Logger logger = LoggerFactory.getLogger(ProcessPaymentState.class);
	
	@Override
	public State getState() {
		return State.PROCESS_PAYMENT;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("process");
		
		String reference;
		
		try {
			reference = BankInterface.processPayment(adventure.getIBAN(), adventure.getAmount());
	} catch (BankException e){
		adventure.setState(State.CANCELLED);
		return;
	}catch (RemoteAccessException be) {
		incNumOfRemoteErrors();
		if (getNumOfRemoteErrors() == 3){
			adventure.setState(State.CANCELLED);
		}
		return;
	}
		adventure.setState(State.RESERVE_ACTIVITY);
		
		adventure.setPaymentConfirmation(reference);
		System.out.println("Payment processed: " + reference);
		System.out.println("IBAN: " + adventure.getIBAN());
		System.out.println("Amount: " + adventure.getAmount());
		
	}

}
