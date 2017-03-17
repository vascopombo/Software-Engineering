package pt.ulisboa.tecnico.softeng.hotel.domain;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import pt.ulisboa.tecnico.softeng.hotel.exception.HotelException;

public class HotelConstructorTest {

	@Before
	public void setUp() {

	}

	@Test
	public void success() {
		Hotel hotel = new Hotel("XPTO123", "Londres");

		Assert.assertEquals("Londres", hotel.getName());
		Assert.assertTrue(hotel.getCode().length() == Hotel.CODE_SIZE);
		Assert.assertEquals(0, hotel.getNumberOfRooms());
		Assert.assertEquals(1, Hotel.hotels.size());
	}
	
	@Test(expected = HotelException.class)
	public void emptyCode(){
		Hotel hotel= new Hotel("", "Lisboa");
	}
	
	@Test(expected = HotelException.class)
	public void emptyName(){
		Hotel hotel= new Hotel("XPTO123", "");
	}
	
	@Test(expected = HotelException.class)
	public void nullCode(){
		Hotel hotel= new Hotel(null, "Lisboa");
	}
	
	@Test(expected = HotelException.class)
	public void nullName(){
		Hotel hotel= new Hotel("XPTO123", null);
	}
	
	@Test(expected = HotelException.class)
	public void whitespaceCode(){
		Hotel hotel= new Hotel("       ", "Lisboa");
	}
	
	@Test(expected = HotelException.class)
	public void whitespaceName(){
		Hotel hotel= new Hotel("XPTO123", "        ");
	}
	
	@Test(expected = HotelException.class)
	public void invalidCodeLength(){
		Hotel hotel = new Hotel("XPTO13", "Andorra");
	}
	
	@Test(expected = HotelException.class)
	public void invalidCodeLength1(){
		Hotel hotel = new Hotel("XPTO1234", "Andorra");
	}
	
	@Test
	public void validCodeLength(){
		Hotel hotel = new Hotel("XPTO123", "Andorra");
	}
	
	@Test(expected = HotelException.class)
	public void uniqueId() {
		Hotel hotel = new Hotel("XPTO123", "Londres");
		Hotel hotel1 = new Hotel("XPTO123", "Andorra");
	}
	
	@Test
	public void uniqueId1() {
		Hotel hotel = new Hotel("XPTO124", "Londres");
		Hotel hotel1 = new Hotel("XPTO123", "Londres");
	}

	@After
	public void tearDown() {
		Hotel.hotels.clear();
	}

}
