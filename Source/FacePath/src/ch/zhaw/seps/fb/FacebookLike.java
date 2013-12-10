package ch.zhaw.seps.fb;

public class FacebookLike {
	
	private String UUID;
	private String UUIDString;
	private String description;
	private String website;
	private String category;
	
	public FacebookLike(String cUUID, String cUUIDString, String cdescription, String cwebsite, String ccategory) {
		this.UUID = cUUID;
		this.UUIDString = cUUIDString;
		this.description = cdescription;
		this.website = cwebsite;
		this.description = cdescription;
		
	}

	public String getUUID() {
		return UUID;
	}
	public String getUUIDString() {
		return UUIDString;
	}
	public String getDescription() {
		return description;
	}
	public String getWebsite() {
		return website;
	}
	public String getCategory() {
		return category;
	}
}
