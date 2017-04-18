package pt.ulisboa.tecnico.softeng.broker.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;
import pt.ulisboa.tecnico.softeng.hotel.domain.Hotel;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room;
import pt.ulisboa.tecnico.softeng.hotel.domain.Room.Type;

public class BrokerPersistenceTest {
	private static final String BROKER_NAME = "Happy Going";
	private static final String BROKER_CODE = "BK1017";
	private static final int AGE = 20;
	private static final int AMOUNT = 300;
	private static final String IBAN = "BK011234567";

	private final LocalDate begin = new LocalDate(2016, 12, 19);
	private final LocalDate end = new LocalDate(2016, 12, 21);

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		Broker broker = new Broker(BROKER_CODE, BROKER_NAME);
		
		Hotel hotel = new Hotel("XPTO123", "Andorra");
		new Room (hotel, "01", Type.SINGLE);
		new Room (hotel, "02", Type.SINGLE);
		new Room (hotel, "03", Type.SINGLE);

		broker.bulkBooking(3, begin, end);

		new Adventure(broker, this.begin, this.end, AGE, IBAN, AMOUNT);
		
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		assertEquals(1, FenixFramework.getDomainRoot().getBrokerSet().size());

		List<Broker> brokers = new ArrayList<>(FenixFramework.getDomainRoot().getBrokerSet());
		Broker broker = brokers.get(0);

		assertEquals(BROKER_CODE, broker.getCode());
		assertEquals(BROKER_NAME, broker.getName());
		assertEquals(1, broker.getAdventureSet().size());

		List<Adventure> adventures = new ArrayList<>(broker.getAdventureSet());
		Adventure adventure = adventures.get(0);

		assertNotNull(adventure.getID());
		assertEquals(broker, adventure.getBroker());
		assertEquals(this.begin, adventure.getBegin());
		assertEquals(this.end, adventure.getEnd());
		assertEquals(AGE, adventure.getAge());
		assertEquals(IBAN, adventure.getIBAN());
		assertEquals(AMOUNT, adventure.getAmount());

		assertEquals(Adventure.State.PROCESS_PAYMENT, adventure.getState().getValue());
		assertEquals(0, adventure.getState().getNumOfRemoteErrors());
		
		assertEquals(1, broker.getBulkRoomBookingSet().size());
		List<BulkRoomBooking> bulks = new ArrayList<>(broker.getBulkRoomBookingSet());
		BulkRoomBooking bulk = bulks.get(0);
		assertEquals(3, bulk.getReferences().size());
		
		assertEquals(3, bulk.getNumber());
		assertEquals(begin, bulk.getArrival());
		assertEquals(end, bulk.getDeparture());
		
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Broker broker : FenixFramework.getDomainRoot().getBrokerSet()) {
			broker.delete();
		}
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
