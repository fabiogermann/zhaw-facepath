package ch.zhaw.swe.refactoring;

import java.util.Date;

/**
 * Representiert einen Kunden aus der Sicht einer Retail Anwendung. 
 * @author fer
 *
 */
public class Customer extends Person {
	private static final double SENIOR_DISCOUNT_IN_PERCENT = 0.9;
	private long customerId;
	private double totalSale;
	public Customer(String name, String firstName, String street, String plz, String ort,
			long customerId, double totalSale, Date dateOfBirth) {
		super(name, firstName, street, plz, ort, dateOfBirth);
		this.customerId = customerId;
		this.totalSale = totalSale;
	}

	/**
	 * Gibt die eineindeutige Kunden Id zur�ck. 
	 * @return
	 */
	public long customerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

	public double getTotalSale() {
		return totalSale;
	}
	
	public void addTotalSale(double s){
		this.totalSale =+ s;
	}
	
	/**
	 * Berechnet aus dem Totalbetrag eines Einkaufs den 
	 * effektiv zu bezahlenden Betrags unter Ber�cksichtigung
	 * des Rabatts, den ein AHV Berechtigter erh�lt. 
	 * @param s
	 * @return
	 */
	public double correctTotalForSeniorDiscount(double s){
		if (!isRetired()) { 
			// j�nger als 65 Jahre 
			return s;
		}
		return s * SENIOR_DISCOUNT_IN_PERCENT;
	}
}
