package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		int capacity = offer.getCap();
		int nrBookings = offer.getNumberOfBookings();
		bookingValidator(capacity, nrBookings);
		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
	}
	
	public void bookingValidator(int capacity, int nrBookings){
		if(capacity < nrBookings){
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}
}
