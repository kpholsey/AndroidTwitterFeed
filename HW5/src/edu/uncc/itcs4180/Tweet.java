/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

//Tweet user class based on the assignment tutorial by rockncoder
//Added extra setters and getters for additional functionality needed
//for the assignment
public class Tweet implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6695575790789977221L;

	@SerializedName("created_at")
	private String dateCreated;

	@SerializedName("id")
	private String id;

	@SerializedName("text")
	private String text;

	@SerializedName("in_reply_to_status_id")
	private String inReplyToStatusId;

	@SerializedName("in_reply_to_user_id")
	private String inReplyToUserId;

	@SerializedName("in_reply_to_screen_name")
	private String inReplyToScreenName;

	@SerializedName("user")
	private TwitterUser user;

	@SerializedName("retweeted")
	private boolean retweeted;

	@SerializedName("favorite_count")
	private int favoriteCount;

	@SerializedName("retweet_count")
	private int retweetCount;

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public String getDateCreated() {
		return dateCreated;
	}

	public String getId() {
		return id;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public String getInReplyToStatusId() {
		return inReplyToStatusId;
	}

	public String getInReplyToUserId() {
		return inReplyToUserId;
	}

	public String getText() {
		return text;
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public void setInReplyToStatusId(String inReplyToStatusId) {
		this.inReplyToStatusId = inReplyToStatusId;
	}

	public void setInReplyToUserId(String inReplyToUserId) {
		this.inReplyToUserId = inReplyToUserId;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setUser(TwitterUser user) {
		this.user = user;
	}

	public TwitterUser getUser() {
		return user;
	}

	public boolean isRetweet() {
		return retweeted;
	}

	public void setRetweet(boolean retweeted) {
		this.retweeted = retweeted;
	}

	@Override
	public String toString() {
		return getText();
	}
}
