package ch.zhaw.seps.fb;

import java.util.List;

public class FacebookProfile {
	
	private String userName;
	private String name;
	private String familyName;
	private List<FacebookProfile> friends;
	
	public FacebookProfile(String userN) {
		this.userName = userN;
	}
	
	public String getUserUIDString() {
		return this.userName;
	}
	
	public void setName(String n, String fn) {
		this.name = n;
		this.familyName =fn;
	}

}
