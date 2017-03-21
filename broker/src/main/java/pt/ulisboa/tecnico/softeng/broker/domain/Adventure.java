package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;
import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.bank.dataobjects.BankOperationData;
import pt.ulisboa.tecnico.softeng.bank.exception.BankException;
import pt.ulisboa.tecnico.softeng.broker.exception.BrokerException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.ActivityInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.BankInterface;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.dataobjects.RoomBookingData;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class Adventure {
	private static Logger logger = LoggerFactory.getLogger(Adventure.class);

	public static enum State {
		PROCESS_PAYMENT, RESERVE_ACTIVITY, BOOK_ROOM, UNDO, CONFIRMED, CANCELLED
	}

	private static int counter = 0;

	private final String ID;
	private final Broker broker;
	private final LocalDate begin;
	private final LocalDate end;
	private final int age;
	private final String IBAN;
	private final int amount;
	private String paymentConfirmation;
	private String paymentCancellation;
	private String roomConfirmation;
	private String roomCancellation;
	private String activityConfirmation;
	private String activityCancellation;

	private State oldState; // to be removed once all states are refactored as
							// subclasses of AdventureState
	private AdventureState state;

	public Adventure(Broker broker, LocalDate begin, LocalDate end, int age, String IBAN, int amount) {
		checkArguments(broker, begin, end, age, IBAN, amount);

		this.ID = broker.getCode() + Integer.toString(++counter);
		this.broker = broker;
		this.begin = begin;
		this.end = end;
		this.age = age;
		this.IBAN = IBAN;
		this.amount = amount;

		broker.addAdventure(this);

		setState(State.PROCESS_PAYMENT);
	}

	private void checkArguments(Broker broker, LocalDate begin, LocalDate end, int age, String IBAN, int amount) {
		if (broker == null || begin == null || end == null || IBAN == null || IBAN.trim().length() == 0) {
			throw new BrokerException();
		}

		if (end.isBefore(begin)) {
			throw new BrokerException();
		}

		if (age < 18 || age > 100) {
			throw new BrokerException();
		}

		if (amount < 1) {
			throw new BrokerException();
		}
	}

	public String getID() {
		return this.ID;
	}

	public Broker getBroker() {
		return this.broker;
	}

	public LocalDate getBegin() {
		return this.begin;
	}

	public LocalDate getEnd() {
		return this.end;
	}

	public int getAge() {
		return this.age;
	}

	public String getIBAN() {
		return this.IBAN;
	}

	public int getAmount() {
		return this.amount;
	}

	public String getPaymentConfirmation() {
		return this.paymentConfirmation;
	}

	public void setPaymentConfirmation(String paymentConfirmation) {
		this.paymentConfirmation = paymentConfirmation;
	}

	public String getPaymentCancellation() {
		return this.paymentCancellation;
	}

	public void setPaymentCancellation(String paymentCancellation) {
		this.paymentCancellation = paymentCancellation;
	}

	public String getActivityConfirmation() {
		return this.activityConfirmation;
	}

	public void setActivityConfirmation(String activityConfirmation) {
		this.activityConfirmation = activityConfirmation;
	}

	public String getActivityCancellation() {
		return this.activityCancellation;
	}

	public void setActivityCancellation(String activityCancellation) {
		this.activityCancellation = activityCancellation;
	}

	public String getRoomConfirmation() {
		return this.roomConfirmation;
	}

	public void setRoomConfirmation(String roomConfirmation) {
		this.roomConfirmation = roomConfirmation;
	}

	public String getRoomCancellation() {
		return this.roomCancellation;
	}

	public void setRoomCancellation(String roomCancellation) {
		this.roomCancellation = roomCancellation;
	}

	public State getState() {
		switch (this.oldState) {
		case PROCESS_PAYMENT:
		case RESERVE_ACTIVITY:
		case BOOK_ROOM:
		case UNDO:
		case CONFIRMED:
			return this.oldState;
		case CANCELLED:
			return this.state.getState();
		default:
			new BrokerException();
			return null;
		}
	}

	public void setState(State state) {
		this.oldState = state;
		switch (state) {
		case PROCESS_PAYMENT:
			this.state = null;
			break;
		case RESERVE_ACTIVITY:
			this.state = null;
			break;
		case BOOK_ROOM:
			this.state = null;
			break;
		case UNDO:
			this.state = null;
			break;
		case CONFIRMED:
			this.state = null;
			break;
		case CANCELLED:
			this.state = new CancelledState();
			break;
		default:
			new BrokerException();
			break;

		}
	}

	public void process() {
		logger.debug("process ID:{}, state:{} ", this.ID, this.oldState.name());

		switch (this.oldState) {
		case PROCESS_PAYMENT:
			try {
				this.paymentConfirmation = BankInterface.processPayment(this.IBAN, this.amount);
			} catch (BankException be) {
				setState(State.CANCELLED);
			} catch (RemoteAccessException rae) {
				// increment number of errors
				// if (number of errors == 3) {
				// setState(State.CANCELLED);
				// }
				return;
			}

			setState(State.RESERVE_ACTIVITY);

			break;
		case RESERVE_ACTIVITY:
			try {
				this.activityConfirmation = ActivityInterface.reserveActivity(this.begin, this.end, this.age);
			} catch (ActivityException ae) {
				setState(State.UNDO);
			} catch (RemoteAccessException rae) {
				state.incNumOfRemoteErrors();
				if (state.getNumOfRemoteErrors() == 5){
					setState(State.UNDO);
				}
			}
			if (this.begin.equals(this.end)) {
				setState(State.CONFIRMED);
			} else {
				setState(State.BOOK_ROOM);
			}

			break;
		case BOOK_ROOM:
			try {
				this.roomConfirmation = HotelInterface.reserveRoom(Room.Type.SINGLE, this.begin, this.end);
			} catch (HotelException rae) {
				setState(State.UNDO);
			} catch (RemoteAccessException rae) {
				// increment number of errors
				// if (number of errors == 10) {
				// adventure.setState(State.UNDO);
				// }
				// return;
			}

			setState(State.CONFIRMED);

			break;
		case UNDO:
			if (cancelPayment()) {
				try {
					this.paymentCancellation = BankInterface.cancelPayment(getPaymentConfirmation());
				} catch (HotelException | RemoteAccessException ex) {
					// does not change state
				}
			}

			if (cancelActivity()) {
				try {
					this.activityCancellation = ActivityInterface.cancelReservation(getActivityConfirmation());
				} catch (HotelException | RemoteAccessException ex) {
					// does not change state
				}
			}

			if (cancelRoom()) {
				try {
					this.roomCancellation = HotelInterface.cancelBooking(getRoomConfirmation());
				} catch (HotelException | RemoteAccessException ex) {
					// does not change state
				}
			}

			if (!cancelPayment() && !cancelActivity() && !cancelRoom()) {
				setState(State.CANCELLED);
			}
			break;
		case CONFIRMED:
			BankOperationData operation;
			try {
				operation = BankInterface.getOperationData(getPaymentConfirmation());
			} catch (BankException be) {
				// increment number of errors
				// if (number of errors == 5) {
				// adventure.setState(State.UNDO);
				// }
				// return;
			} catch (RemoteAccessException rae) {
				// increment number of errors
				// if (number of errors == 20) {
				// adventure.setState(State.UNDO);
				// }
				// return;
			}
			// reset number of errors

			ActivityReservationData reservation;
			try {
				reservation = ActivityInterface.getActivityReservationData(getActivityConfirmation());
			} catch (ActivityException ae) {
				setState(State.UNDO);
				return;
			} catch (RemoteAccessException rae) {
				// increment number of errors
				// if (number of errors == 20) {
				// adventure.setState(State.UNDO);
				// }
				// return;
			}
			// reset number of errors

			if (getRoomConfirmation() != null) {
				RoomBookingData booking;
				try {
					booking = HotelInterface.getRoomBookingData(getRoomConfirmation());
				} catch (HotelException he) {
					setState(State.UNDO);
					return;
				} catch (RemoteAccessException rae) {
					// increment number of errors
					// if (number of errors == 20) {
					// adventure.setState(State.UNDO);
					// }
					// return;
				}
				// reset number of errors
			}

			// TODO: prints the complete Adventure file, the info in operation,
			// reservation and booking

			break;
		case CANCELLED:
			this.state.process(this);
			break;
		default:
			throw new BrokerException();
		}
	}

	public boolean cancelRoom() {
		return getRoomConfirmation() != null && getRoomCancellation() == null;
	}

	public boolean cancelActivity() {
		return getActivityConfirmation() != null && getActivityCancellation() == null;
	}

	public boolean cancelPayment() {
		return getPaymentConfirmation() != null && getPaymentCancellation() == null;
	}

}
