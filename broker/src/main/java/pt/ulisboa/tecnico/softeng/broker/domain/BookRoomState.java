package pt.ulisboa.tecnico.softeng.broker.domain;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;


public class BookRoomState extends AdventureState {
	private static Logger logger = LoggerFactory.getLogger(BookRoomState.class);
	
	@Override
	public State getState() {
		return State.BOOK_ROOM;
	}

	@Override
	public void process(Adventure adventure) {
		logger.debug("process");

			String reference = null;
			try {
				reference = HotelInterface.reserveRoom(Room.Type.SINGLE, adventure.getBegin(), adventure.getEnd());
			}  catch (HotelException rae) {
				adventure.setState(State.UNDO);
			} catch (RemoteAccessException rae) {
				incNumOfRemoteErrors();
				if (getNumOfRemoteErrors() == 10){
					adventure.setState(State.UNDO);
				}
				return;
			}
			
			adventure.setRoomConfirmation(reference);
			System.out.println("Booked Room: " + reference);
			System.out.println("Arrival: " + adventure.getBegin());
			System.out.println("Departure: " + adventure.getEnd());
			
			adventure.setState(State.CONFIRMED);
			
		}

}
