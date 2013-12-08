package ch.zhaw.swe.refactoring;

import java.util.Date;

/**
 * Representiert eine Person mit ihren wichtigsten Daten
 * @author fer
 *
 */
public class Person implements Addressable {
	protected static final long RETIREMENT_AGE_IN_MILLISECONDS = 65*365*24*3600*1000L;
	protected String name;
	protected String firstName;
	protected String street;
	protected String plz;
	protected String place;
	protected Date dateOfBirth;
	
	public Person(String name, String firstName, String street, String plz, String place, Date dateOfBirth) {
		super();
		this.name = name;
		this.firstName = firstName;
		this.street = street;
		this.plz = plz;
		this.place = place;
		this.dateOfBirth = dateOfBirth;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPlz() {
		return plz;
	}

	public void setPlz(String plz) {
		this.plz = plz;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String ort) {
		this.place = ort;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreet() {
		return street;
	}

	/**
	 * @return whether Employee is retired
	 */
	protected boolean isRetired() {
		return (new Date().getTime() - getDateOfBirth().getTime()) > RETIREMENT_AGE_IN_MILLISECONDS;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
}
