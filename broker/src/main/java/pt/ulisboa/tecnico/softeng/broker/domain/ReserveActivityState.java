package pt.ulisboa.tecnico.softeng.broker.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;


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
			} catch (ActivityException e){
				adventure.setState(State.UNDO);
				return;
				
			}catch (RemoteAccessException be) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == 5){
					adventure.setState(State.UNDO);
				}
				return;
			}
			
			if (adventure.getBegin().equals(adventure.getEnd())) {
				adventure.setState(State.CONFIRMED);
			} else {
				adventure.setState(State.BOOK_ROOM);
			}
				
			adventure.setActivityConfirmation(reference);
			System.out.println("Activity reserved: " + reference);
			System.out.println("Begin: " + adventure.getBegin());
			System.out.println("End: " + adventure.getEnd());
			System.out.println("Broker: " + adventure.getBroker());
			
		}
}
