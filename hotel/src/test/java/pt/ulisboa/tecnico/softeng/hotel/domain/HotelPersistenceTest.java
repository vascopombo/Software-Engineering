package pt.ulisboa.tecnico.softeng.hotel.domain;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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
		
		assertEquals(1, FenixFramework.getDomainRoot().getHotelSet().size());

		List<Hotel> hotels = new ArrayList<>(FenixFramework.getDomainRoot().getHotelSet());
		Hotel hotel = hotels.get(0);

		assertEquals(HOTEL_CODE, hotel.getCode());
		assertEquals(HOTEL_NAME, hotel.getName());

	}

	@After
	@Atomic(mode = TxMode.WRITE)
	public void tearDown() {
		for (Hotel hotel : FenixFramework.getDomainRoot().getHotelSet()) {
			hotel.delete();
		}
	}

}
