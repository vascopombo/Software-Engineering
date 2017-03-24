package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class ReserveActivityState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(ReserveActivityState.class);
	
	@Override
	public State getState() {
		return State.RESERVE_ACTIVITY;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("process");

			String reference;
			try {
				reference = ActivityInterface.reserveActivity(adventure.getBegin(), adventure.getEnd(), adventure.getAge());
			} catch (ActivityException | RemoteAccessException e) {
				return;
			}
			
			adventure.setActivityConfirmation(reference);
			System.out.println("Activity reserved: " + reference);
			System.out.println("Begin: " + adventure.getBegin());
			System.out.println("End: " + adventure.getEnd());
			System.out.println("Broker: " + adventure.getBroker());
			
		}


}
