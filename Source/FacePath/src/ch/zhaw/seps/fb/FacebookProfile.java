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
	private List<String> candidate = new ArrayList<String>();
	private String profileLink;
	
	
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
	
	public void addCandidate(String f) {
		if(f != null) {
			this.candidate.add(f);
		}
	}
	
	public List<String> getCandidates() {
		return this.candidate;
	}
}
