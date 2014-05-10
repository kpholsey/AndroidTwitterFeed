/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

//Creates a saved tweet class with a few constructors and standard
//setters and getters
public class SavedTweet {
	private long _id;
	private String userName;
	private String text;
	private String profileBackgroundImage;
	private int favoriteCount;
	private int retweetsCount;
	private String time;

	public SavedTweet(String userName, String text,
			String profileBackgroundImage, int favoriteCount,
			int retweetsCount, String time) {
		super();
		this.userName = userName;
		this.text = text;
		this.profileBackgroundImage = profileBackgroundImage;
		this.favoriteCount = favoriteCount;
		this.retweetsCount = retweetsCount;
		this.time = time;
	}

	public SavedTweet() {
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getProfileBackgroundImage() {
		return profileBackgroundImage;
	}

	public void setProfileBackgroundImage(String profileBackgroundImage) {
		this.profileBackgroundImage = profileBackgroundImage;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public int getRetweetsCount() {
		return retweetsCount;
	}

	public void setRetweetsCount(int retweetsCount) {
		this.retweetsCount = retweetsCount;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return userName + ": " + text + " " + "Image: "
				+ profileBackgroundImage + " Favorites Count: " + favoriteCount
				+ " Retweet Count: " + retweetsCount + " Date: " + time;
	}

}
