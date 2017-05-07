package pt.ulisboa.tecnico.softeng.activity.services.local.dataobjects;

import java.util.Set;
import org.joda.time.LocalDate;


import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.Booking;

public class ActivityOfferData {
		
	private LocalDate begin;
	private LocalDate end;
	private int capacity;
	private Set<ActivityReservationData> bookings;
	
	public ActivityOfferData() {
		
	}
	
	public ActivityOfferData(ActivityOffer activityOffer) {
		this.begin = activityOffer.getBegin();
		this.end = activityOffer.getEnd();
		this.capacity = activityOffer.getCapacity();
		
		for (Booking booking : activityOffer.getBookingSet()) {
			this.bookings.add(new ActivityReservationData(activityOffer.getActivity().getActivityProvider(), activityOffer, booking));
		}
	}
	
	public LocalDate getBegin() {
		return this.begin;
	}
	
	public void setBegin(LocalDate begin) {
		this.begin = begin;
	}
	
	public LocalDate getEnd() {
		return this.end;
	}
	
	public void setEnd(LocalDate end) {
		this.end = end;
	}
	
	public int getCapacity() {
		return this.capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public Set<ActivityReservationData> getBookings() {
		return this.bookings;
	}
	
	public void setBookings (Set<ActivityReservationData> bookings){
		this.bookings = bookings;
	}
}