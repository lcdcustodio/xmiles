package com.xmiles.android.listviewfeed;

public class LikeItem {
	private int id;
	private String name, profilePic, timeStamp;

	public LikeItem() {
	}

	public LikeItem(int id, String name,
			String profilePic, String timeStamp) {
		super();
		this.id = id;
		this.name = name;

		this.profilePic = profilePic;
		this.timeStamp = timeStamp;

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
