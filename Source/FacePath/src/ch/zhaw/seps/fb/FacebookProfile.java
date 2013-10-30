package ch.zhaw.seps.fb;

import java.util.List;

public class FacebookProfile {
	
	private String userName;
	private String userID;
	private String name;
	private String familyName;
	private List<FacebookProfile> friends;
	private String profileLink;
	
	
	public FacebookProfile(String userN, String uID) {
		this.userName = userN;
		this.userID = uID;
	}
	
	public String getUserUIDString() {
		return this.userName;
	}
	
	public void setName(String n, String fn) {
		this.name = n;
		this.familyName =fn;
	}

	public String getLink() {
		return this.profileLink;
	}
	
	public void setLink(String l) {
		this.profileLink = l;
	}

	public String getUsername() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getUserID() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getFirstName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getLastName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<FacebookProfile> getFriends() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void setFriends(List<FacebookProfile> fl) {
		// TODO Auto-generated method stub
	}

}
