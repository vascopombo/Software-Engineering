package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class UndoState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(UndoState.class);
	
	

	@Override
	public State getState() {
		return State.UNDO;
	}
	
	@Override
	public void process(Adventure adventure) {
		logger.debug("process");
		
		String reference1 = null;
		String reference2 = null;
		String reference3 = null;
	
	if (adventure.cancelPayment()) {
		try {
			reference1 = BankInterface.cancelPayment(adventure.getPaymentConfirmation());
		} catch (BankException | RemoteAccessException ex) {
			return;
		}
		adventure.setPaymentCancellation(reference1);
	}

	if (adventure.cancelActivity()) {
		try {
			reference2 = ActivityInterface.cancelReservation(adventure.getActivityConfirmation());
		} catch (ActivityException | RemoteAccessException ex) {
			return;
		}
		adventure.setActivityCancellation(reference2);
	}

	if (adventure.cancelRoom()) {
		try {
			reference3 = HotelInterface.cancelBooking(adventure.getRoomConfirmation());
		} catch (HotelException | RemoteAccessException ex) {
			return;
		}
		adventure.setRoomCancellation(reference3);
	}

	if (!adventure.cancelPayment() && !adventure.cancelActivity() && !adventure.cancelRoom()) {
		adventure.setState(State.CANCELLED);
		}
	}
	
}