package com.xmiles.android.listviewfeed;

public class FeedItem {
	private int id;
	private String name, status, image, profilePic, timeStamp, url, like_stats, comment_stats, you_like_this,
				   hashtag_1;

	public FeedItem() {
	}

	public FeedItem(int id, String name, String image, String status,
			String profilePic, String timeStamp, String url, String like_stats, String comment_stats, String you_like_this,
			String hashtag_1) {
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.status = status;
		this.profilePic = profilePic;
		this.timeStamp = timeStamp;
		this.url = url;
		//-----------
		this.like_stats = like_stats;
		this.comment_stats = comment_stats;
		//-----------
		this.you_like_this = you_like_this;
		//-----------
		this.hashtag_1 = hashtag_1;
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

	public String getImge() {
		return image;
	}

	public void setImge(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getLike_stats() {
		return like_stats;
	}

	public void setLike_stats(String like) {
		this.like_stats = like;
	}
	
	public String getComment_stats() {
		return comment_stats;
	}

	public void setComment_stats(String comment) {
		this.comment_stats = comment;
	}

	public String getYou_like_this() {
		return you_like_this;
	}

	public void setYou_like_this(String like_this) {
		this.you_like_this = like_this;
	}

	public String getHashtag_1() {
		return hashtag_1;
	}

	public void setHashtag_1(String hashtag_1) {
		this.hashtag_1 = hashtag_1;
	}	
}
