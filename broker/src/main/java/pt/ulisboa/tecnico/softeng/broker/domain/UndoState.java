package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
		
		String reference1;
		String reference2;
		String reference3;
	
	if (adventure.cancelPayment()) {
		try {
			reference1 = BankInterface.cancelPayment(adventure.getPaymentConfirmation());
		} catch (HotelException | RemoteAccessException ex) {
			return;
		}
	}

	if (adventure.cancelActivity()) {
		try {
			reference2 = ActivityInterface.cancelReservation(adventure.getActivityConfirmation());
		} catch (HotelException | RemoteAccessException ex) {
			return;
		}
	}

	if (adventure.cancelRoom()) {
		try {
			reference3 = HotelInterface.cancelBooking(adventure.getRoomConfirmation());
		} catch (HotelException | RemoteAccessException ex) {
			return;
		}
	}

	if (!adventure.cancelPayment() && !adventure.cancelActivity() && !adventure.cancelRoom()) {
		adventure.setState(State.CANCELLED);
		}
	}
	
}