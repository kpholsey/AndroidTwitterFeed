/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

//Creates a bridge to the DAO for the database
public class DatabaseDataManager {
	private Context mContext;
	private DatabaseOpenHelper dbOpenHelper;
	private SQLiteDatabase db;
	private SavedTweetDAO savedTweetDAO;

	// Constructor
	public DatabaseDataManager(Context mContext) {
		this.mContext = mContext;
		dbOpenHelper = new DatabaseOpenHelper(this.mContext);
		db = dbOpenHelper.getWritableDatabase();
		savedTweetDAO = new SavedTweetDAO(db);
	}

	public void close() {
		if (db != null) {
			db.close();
		}
	}

	public SavedTweetDAO getSavedTweetDAO() {
		return this.savedTweetDAO;
	}

	public long saveSavedTweet(SavedTweet savedTweet) {
		return this.savedTweetDAO.save(savedTweet);
	}

	public boolean updateSavedTweet(SavedTweet savedTweet) {
		return this.savedTweetDAO.update(savedTweet);
	}

	public boolean deleteSavedTweet(SavedTweet savedTweet) {
		return this.savedTweetDAO.delete(savedTweet);
	}

	public SavedTweet getSavedTweet(long id) {
		return this.savedTweetDAO.get(id);
	}

	public List<SavedTweet> getAllSavedTweets() {
		return this.savedTweetDAO.getAll();
	}

	public boolean deleteAllSavedTweets() {
		return this.savedTweetDAO.deleteAll();
	}
}
