package ch.zhaw.swe.refactoring;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit tests f�r Klasse Customer
 * @author fer
 *
 */
public class CustomerTest {
	private Customer customer;
	private Calendar calendar;
	
	@Before
	public void setup(){
		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1980);
		calendar.set(Calendar.MONTH, 9);
		calendar.set(Calendar.DAY_OF_MONTH, 22);
	}
	
	@Test
	public void testCalcAlvNotRetired(){
		customer = new Customer("Name", "FirstName", "Teststreet 12", "8400", "Winterthur", 100, 1541d, calendar.getTime());
		assertEquals(150, customer.correctTotalForSeniorDiscount(150), 1e-5);
	}

	/**
	 * 
	 */
	@Test
	public void testCalcAlvRetired() {
		calendar.set(Calendar.YEAR, 1930);
		customer = new Customer("Name", "FirstName", "Wurstweg 8", "8400", "Winterthur", 100, 1541d, calendar.getTime());
		assertEquals(135, customer.correctTotalForSeniorDiscount(150), 1e-5);
	}

}
