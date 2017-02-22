package pt.ulisboa.tecnico.softeng.broker;

import org.joda.time.LocalDate;

import pt.ulisboa.tecnico.softeng.bank.domain.Account;
import pt.ulisboa.tecnico.softeng.bank.domain.Bank;
import pt.ulisboa.tecnico.softeng.bank.domain.Client;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure;
import pt.ulisboa.tecnico.softeng.broker.domain.Broker;
import pt.ulisboa.tecnico.softeng.hotel.domain.Booking;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityProvider;
import pt.ulisboa.tecnico.softeng.activity.domain.ActivityOffer;
import pt.ulisboa.tecnico.softeng.activity.domain.Activity;


public class Application {

	public static void main(String[] args) {
		System.out.println("Adventures!");

		Bank bank = new Bank("MoneyPlus", "BK01");
		Account account = new Account(bank, new Client(bank, "José dos Anzóis"));
		account.deposit(1000);

		Broker broker = new Broker("BR01", "Fun");
		Adventure adventure = new Adventure(broker, new LocalDate(), new LocalDate(), 33, account.getIBAN(), 50);

		adventure.process();
		
		Hotel hotel = new Hotel("HOTEL01", "Marina");
		Booking booking = new Booking(hotel, new LocalDate(), new LocalDate());
		
		ActivityProvider activeprov = new ActivityProvider("ACTI01", "Sporjovem");
		Activity activity = new Activity(activeprov, "Rapel", 15, 88, 10);
		ActivityOffer activeoffer = new ActivityOffer(activity, new LocalDate(), new LocalDate());
		pt.ulisboa.tecnico.softeng.activity.domain.Booking activbooking = new pt.ulisboa.tecnico.softeng.activity.domain.Booking(activeprov, activeoffer);
		
		
		System.out.println("Your payment reference is " + adventure.getBankPayment() + " and you have "
				+ account.getBalance() + " euros left in your account");
		
		System.out.println("Your hotel booking reference is " + booking.getReference());
		
		System.out.println("Your activity booking reference is " + activbooking.getReference());
		
		
	}

}
