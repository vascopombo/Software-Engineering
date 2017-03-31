package pt.ulisboa.tecnico.softeng.activity.domain;

import pt.ulisboa.tecnico.softeng.activity.exception.ActivityException;
import pt.ulisboa.tecnico.softeng.activity.dataobjects.ActivityReservationData;

public class Booking {
	private static int counter = 0;

	private final String reference;

	public Booking(ActivityProvider provider, ActivityOffer offer) {
		checkArguments(provider, offer);

		this.reference = provider.getCode() + Integer.toString(++Booking.counter);

		offer.addBooking(this);
		provider.addActivityReservationData(newActivityReservationData(provider, offer));
	}

	private void checkArguments(ActivityProvider provider, ActivityOffer offer) {
		if (provider == null || offer == null) {
			throw new ActivityException();
		}

	}

	public String getReference() {
		return this.reference;
	}
	
	public ActivityReservationData newActivityReservationData( ActivityProvider provider, ActivityOffer offer){
		if (provider == null || offer == null) {
			throw new ActivityException();
		}
		else{
			ActivityReservationData data = new ActivityReservationData();
			data.constructorActivityReservationData(reference, provider.getName(), provider.getCode(), 
					offer.getBegin(), offer.getEnd());
	
			return data;
		}
	}
}
