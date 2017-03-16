package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.domain.exception.ActivityException;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		this.reference = provider.getCode() + Integer.toString(++Booking.counter);
		
		int capacity = offer.getCap();
		int nrBookings = offer.getNumberOfBookings();
		bookingValidator(capacity, nrBookings);

		offer.addBooking(this);
	}
	
	public void bookingValidator(int capacity, int nrBookings){
		if(capacity < nrBookings + 1){
			throw new ActivityException();
		}
	}

	public String getReference() {
		return this.reference;
	}
}
