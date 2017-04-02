package pt.ulisboa.tecnico.softeng.broker.domain;


import java.util.HashSet;
import java.util.Set;

import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import mockit.Expectations;
import mockit.Injectable;
import mockit.Mocked;
import mockit.integration.junit4.JMockit;
import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;
import pt.ulisboa.tecnico.softeng.broker.exception.RemoteAccessException;
import pt.ulisboa.tecnico.softeng.broker.interfaces.HotelInterface;


@RunWith(JMockit.class)
public class BulkRoomBookingTest {
	private final int number=3;
	private final Set<String> references = new HashSet<>();
	private final LocalDate arrival= new LocalDate(2016, 12, 19);
	private final LocalDate departure=new LocalDate(2016, 12, 22);

	private BulkRoomBooking bulkRoomBooking;



	@Injectable
	private Broker broker;

	@Before
	public void setUp() {

		this.bulkRoomBooking = new BulkRoomBooking(this.number, this.arrival, this.departure);
		references.add("ref1");
		references.add("ref2");
		references.add("ref3");
		}

	@Test
	public void Success(@Mocked final HotelInterface hotelInterface) {
			
			new Expectations() {
				{
					HotelInterface.bulkBooking(number, arrival, departure);
					this.result = references;
				}
			};
			this.bulkRoomBooking.processBooking();

			Assert.assertNotEquals(0, bulkRoomBooking.getReferences().size());

		}
	
	@Test
	public void BookRoomFailedFirstException(@Mocked final HotelInterface hotelInterface) {
			new Expectations() {
				{
					HotelInterface.bulkBooking(number, arrival, departure);
					this.result = new HotelException();
				}
			};
			for (int i=0;i<4;i++){
				this.bulkRoomBooking.processBooking();
			}
			Assert.assertEquals(Boolean.TRUE, bulkRoomBooking.getCancel());
	}
	
	@Test
	public void BookRoomFailedSecondException(@Mocked final HotelInterface hotelInterface) {
			new Expectations() {
				{
					HotelInterface.bulkBooking(number, arrival, departure);
					this.result = new RemoteAccessException();
				}
			};
			for (int i=0;i<11;i++){
				this.bulkRoomBooking.processBooking();
			}
			Assert.assertEquals(Boolean.TRUE, bulkRoomBooking.getCancel());
	}
}
