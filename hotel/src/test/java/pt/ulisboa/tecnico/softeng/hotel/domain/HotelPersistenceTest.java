package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Test;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;
import pt.ist.fenixframework.FenixFramework;

public class HotelPersistenceTest {
	
	private static final String HOTEL_CODE = "XPTO123";
	private static final String HOTEL_NAME = "Londres";

	@Test
	public void success() {
		atomicProcess();
		atomicAssert();
	}

	@Atomic(mode = TxMode.WRITE)
	public void atomicProcess() {
		new Hotel(HOTEL_CODE, HOTEL_NAME);
	}

	@Atomic(mode = TxMode.READ)
	public void atomicAssert() {
		
		Hotel hotel = Hotel.getHotelByCode(HOTEL_CODE);
		
		assertEquals(HOTEL_CODE, hotel.getCode());
	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
