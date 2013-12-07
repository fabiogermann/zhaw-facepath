/**
 * Stellt ein einzelnes Profil dar und bietet die Struktur, um alle benötigten Daten eines Profils abzufüllen
 * @author		SEPS Gruppe 2
 */
package ch.zhaw.seps.fb;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class FacebookProfile {
	
	private String userName;
	private String userID;
	private String name;
	private String familyName;
	private Collection<FacebookProfile> friends = new ArrayList<FacebookProfile>();
	private String profileLink;
	private String location;
	private String language;
	
	/**
	 * Konstruktor
	 * Setzt das Minimum an Informationen, das für die Suche benötigt wird
	 */
	public FacebookProfile(String userN, String uID) {
		this.userName = userN;
		this.userID = uID;
	}
	
	public String getUserUIDString() {
		return this.userName;
	}
	
	public String getUserID() {
		return this.userID;
	}
	
	
	public void setName(String n, String fn) {
		this.name = n;
		this.familyName =fn;
	}
	
	public void setLocales(String locales) {
		if(locales != "null") {
			String[] locale = locales.split("_");
			this.language = locale[0];
			this.location = locale[1];
		}
	}
	
	public String getFirstName() {
		return this.name;
	}

	public void setLink(String l) {
		this.profileLink = l;
	}
	
	public String getLink() {
		return this.profileLink;
	}

	public String getLastName() {
		return this.familyName;
	}
	
	public void setFriends(Collection<FacebookProfile> fl) {
		this.friends.addAll(fl);
	}
	
	public Collection<FacebookProfile> getFriends() {
		return this.friends;
	}
	
	public void addFriends(FacebookProfile fp) {
		this.friends.add(fp);
	}

	public String toString() {
		return this.name + " " + this.familyName + " (" + this.userName + ")";
	}
}
