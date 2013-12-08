package ch.zhaw.swe.refactoring;

import java.util.Date;

/**
 * Representiert einen Kunden aus der Sicht einer Retailanwendung
 * @author fer
 *
 */
public class Employee extends Person {
	private static final int MAX_ALV = 2331;
	private static final double PERCENT_LEVEL2 = 0.005d;
	private static final double PERCENT_LEVEL1 = 0.011d;
	private static final int SALARY_YEAR_LEVEL2 = 315000;
	private static final int SALARY_YEAR_LEVEL1 = 126000;
	private long employeeId;
	private double salary;
	public Employee(String name, String firstName, String street, String plz, String place,
			long employeeId, double salary, Date dateOfBirth) {
		super(name, firstName, street, plz, place, dateOfBirth);
		this.employeeId = employeeId;
		this.salary = salary;
	}

	public long getId() {
		return employeeId;
	}

	public void setId(long employeeId) {
		this.employeeId = employeeId;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}

	/**
	 * Berechnet den monatlichen 5 Beitrag an die 
	 * Arbeitslosenversicherung. Bis zu einem Jahreslohn von 126000 muss der
	 * Arbeitnehmer 1.1% bezahlen, f�r den Teil zwischen 126000 und 315000 0.5% und
	 * dar�ber nichts mehr. 
	 * Siehe http://www.ahv-iv.info/andere/00134/00139/index.html
	 * @return
	 */
	public double calcMonthlyALVTax(){
		if (isRetired()) { 
			return 0;
		}
		if (salary < SALARY_YEAR_LEVEL1){	
			return salary * PERCENT_LEVEL1;
		}
		if (salary < SALARY_YEAR_LEVEL2){
			return (salary - SALARY_YEAR_LEVEL1) * PERCENT_LEVEL2 + PERCENT_LEVEL1*SALARY_YEAR_LEVEL1; 
		}
		return MAX_ALV;
	}

}
