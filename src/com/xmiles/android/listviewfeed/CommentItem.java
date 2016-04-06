package com.xmiles.android.listviewfeed;

public class CommentItem {
	private int id;
	private String name, comment, profilePic, timeStamp;

	public CommentItem() {
	}

	public CommentItem(int id, String name, String comment,
			String profilePic, String timeStamp) {
		super();
		this.id = id;
		this.name = name;

		this.comment = comment;
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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
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
