/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

//Adapted from Shehab's tutorial...creates a 
//DAO used to perform CRUD operations on the database
public class SavedTweetDAO {
	private SQLiteDatabase db;

	public SavedTweetDAO(SQLiteDatabase db) {
		this.db = db;
	}

	public long save(SavedTweet savedTweet) {
		ContentValues values = new ContentValues();
		values.put(SavedTweetsTable.COLUMN_USERNAME, savedTweet.getUserName());
		values.put(SavedTweetsTable.COLUMN_TEXT, savedTweet.getText());
		values.put(SavedTweetsTable.COLUMN_BACKGROUND,
				savedTweet.getProfileBackgroundImage());
		values.put(SavedTweetsTable.COLUMN_FAVORITES,
				savedTweet.getFavoriteCount());
		values.put(SavedTweetsTable.COLUMN_RETWEETS,
				savedTweet.getRetweetsCount());
		values.put(SavedTweetsTable.COLUMN_TIME, savedTweet.getTime());
		return db.insert(SavedTweetsTable.TABLENAME, null, values);
	}

	public boolean update(SavedTweet savedTweet) {
		ContentValues values = new ContentValues();
		values.put(SavedTweetsTable.COLUMN_USERNAME, savedTweet.getUserName());
		values.put(SavedTweetsTable.COLUMN_TEXT, savedTweet.getText());
		values.put(SavedTweetsTable.COLUMN_BACKGROUND,
				savedTweet.getProfileBackgroundImage());
		values.put(SavedTweetsTable.COLUMN_FAVORITES,
				savedTweet.getFavoriteCount());
		values.put(SavedTweetsTable.COLUMN_RETWEETS,
				savedTweet.getRetweetsCount());
		values.put(SavedTweetsTable.COLUMN_TIME, savedTweet.getTime());
		return db.update(SavedTweetsTable.TABLENAME, values,
				SavedTweetsTable.COLUMN_ID + "=?",
				new String[] { savedTweet.get_id() + "" }) > 0;
	}

	public boolean delete(SavedTweet savedTweet) {
		return db.delete(SavedTweetsTable.TABLENAME, SavedTweetsTable.COLUMN_ID
				+ "=?", new String[] { savedTweet.get_id() + "" }) > 0;
	}

	@SuppressLint("NewApi")
	public SavedTweet get(long id) {
		SavedTweet savedTweet = null;
		Cursor c = db.query(true, SavedTweetsTable.TABLENAME,
				new String[] { SavedTweetsTable.COLUMN_ID,
						SavedTweetsTable.COLUMN_USERNAME,
						SavedTweetsTable.COLUMN_TEXT,
						SavedTweetsTable.COLUMN_BACKGROUND,
						SavedTweetsTable.COLUMN_FAVORITES,
						SavedTweetsTable.COLUMN_RETWEETS,
						SavedTweetsTable.COLUMN_TIME },
				SavedTweetsTable.COLUMN_ID + "=?", new String[] { id + "" },
				null, null, null, null, null);

		if (c != null && c.moveToFirst()) {
			savedTweet = buildSavedTweetFromCursor(c);
			if (!c.isClosed()) {
				c.close();
			}
		}
		return savedTweet;
	}

	public List<SavedTweet> getAll() {

		List<SavedTweet> savedTweets = new ArrayList<SavedTweet>();
		Cursor c = db.query(SavedTweetsTable.TABLENAME,
				new String[] { SavedTweetsTable.COLUMN_ID,
						SavedTweetsTable.COLUMN_USERNAME,
						SavedTweetsTable.COLUMN_TEXT,
						SavedTweetsTable.COLUMN_BACKGROUND,
						SavedTweetsTable.COLUMN_FAVORITES,
						SavedTweetsTable.COLUMN_RETWEETS,
						SavedTweetsTable.COLUMN_TIME }, null, null, null, null,
				null);

		if (c != null && c.moveToFirst()) {
			do {
				SavedTweet savedTweet = buildSavedTweetFromCursor(c);
				if (savedTweet != null) {
					savedTweets.add(savedTweet);
				}
			} while (c.moveToNext());

			if (!c.isClosed()) {
				c.close();
			}
		}
		return savedTweets;
	}

	private SavedTweet buildSavedTweetFromCursor(Cursor c) {
		SavedTweet savedTweet = null;
		if (c != null) {
			savedTweet = new SavedTweet();
			savedTweet.set_id(c.getLong(0));
			savedTweet.setUserName(c.getString(1));
			savedTweet.setText(c.getString(2));
			savedTweet.setProfileBackgroundImage(c.getString(3));
			savedTweet.setFavoriteCount(c.getInt(4));
			savedTweet.setRetweetsCount(c.getInt(5));
			savedTweet.setTime(c.getString(6));
		}
		return savedTweet;
	}

	// Drops all the rows in the table and returns the number of rows deleted
	public boolean deleteAll() {
		return db.delete(SavedTweetsTable.TABLENAME, "1", null) > 0;
	}
}
