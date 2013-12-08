package ch.zhaw.swe.refactoring;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;


public class EmployeeTest {
	private Employee employee;
	private Calendar calendar;
	
	@Before
	public void setup(){
		calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, 1980);
		calendar.set(Calendar.MONTH, 9);
		calendar.set(Calendar.DAY_OF_MONTH, 22);
		employee = new Employee("Name", "FirstName", "Musterplatz 1", "8400", "Winterthur", 34567890, 100000, calendar.getTime());
	}
	
	@Test
	public void testCalcAlv(){
		// test for non retired employee
		assertEquals(1100d, employee.calcMonthlyALVTax(), 1e-5);
		
		employee.setSalary(156000d);
		assertEquals(1536d, employee.calcMonthlyALVTax(), 1e-5);
		
		employee.setSalary(390000d);
		assertEquals(2331d, employee.calcMonthlyALVTax(), 1e-5);
		
		// test for retired employee
		calendar.set(Calendar.YEAR, 1930);
		employee.setDateOfBirth(calendar.getTime());

		assertEquals(0d, employee.calcMonthlyALVTax(), 1e-5);
		
		employee.setSalary(156000d);
		assertEquals(0d, employee.calcMonthlyALVTax(), 1e-5);
		
		employee.setSalary(390000d);
		assertEquals(0d, employee.calcMonthlyALVTax(), 1e-5);
	}
}
