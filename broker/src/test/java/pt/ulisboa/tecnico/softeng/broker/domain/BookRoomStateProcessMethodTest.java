package pt.ulisboa.tecnico.softeng.broker.domain;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.StrictExpectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.domain.Adventure.State;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;


@RunWith(JMockit.class)
public class BookRoomStateProcessMethodTest {
	
	private static final String IBAN = "BK01987654321";
	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);
	int i = 0;
	
	private Adventure adventure;

	@Injectable
	private Broker broker;

	@Before
	public void setUp() {
		this.adventure = new Adventure(this.broker, this.begin, this.end, 20, IBAN, 300);
		this.adventure.setState(State.BOOK_ROOM);
		}

	
	@Test
	public void BookRoomFailedFirstException(@Mocked final HotelInterface hotelInterface) {

			new StrictExpectations() {
				{
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
					this.result = new HotelException();
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		}
 

	@Test
	public void BookRoomFailedFirstRemoteException(@Mocked final HotelInterface hotelInterface) {

			new StrictExpectations() {
				{
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
					this.result = new RemoteAccessException();
				}
			};

			this.adventure.process();

			Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		}

	@Test
	public void BookRoomFailedTenthRemoteException(@Mocked final HotelInterface hotelInterface) {

			new StrictExpectations() {
				{
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
					this.result = new RemoteAccessException();
					this.times = 10;
					
				}
			};

			for(i=0;i<10;i++){
			this.adventure.process();
		}	
			
			Assert.assertEquals(Adventure.State.UNDO, this.adventure.getState());
		}

	@Test
	public void BookRoomFailedUpperLimitRemoteException(@Mocked final HotelInterface hotelInterface) {

			new StrictExpectations() {
				{
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
					this.result = new RemoteAccessException();
					this.times = 9;
				}
			};

			for(i=0;i<9;i++){
			this.adventure.process();
		}	
			
			Assert.assertEquals(Adventure.State.BOOK_ROOM, this.adventure.getState());
		}


	@Test
	public void SucessBookedRoom(@Mocked final HotelInterface hotelInterface) {
		
		
			new StrictExpectations() {
				{
					HotelInterface.reserveRoom(Room.Type.SINGLE, begin, end);
					this.result = this.anyString;					
				}
			};

			this.adventure.process();
			
			Assert.assertEquals(Adventure.State.CONFIRMED, this.adventure.getState());
		}
}