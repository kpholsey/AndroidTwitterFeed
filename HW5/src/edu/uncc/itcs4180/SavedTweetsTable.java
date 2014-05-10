/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

//Adapted from Shehab's tutorial...creates a basic table to 
//store the saved tweets
public class SavedTweetsTable {
	static final String TABLENAME = "tweets";
	static final String COLUMN_ID = "_id";
	static final String COLUMN_USERNAME = "username";
	static final String COLUMN_TEXT = "text";
	static final String COLUMN_BACKGROUND = "background";
	static final String COLUMN_FAVORITES = "favorites";
	static final String COLUMN_RETWEETS = "retweets";
	static final String COLUMN_TIME = "time";

	static public void onCreate(SQLiteDatabase db) {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE " + TABLENAME + " (");
		sb.append(COLUMN_ID + " integer primary key autoincrement, ");
		sb.append(COLUMN_USERNAME + " text, ");
		sb.append(COLUMN_TEXT + " text, ");
		sb.append(COLUMN_BACKGROUND + " text, ");
		sb.append(COLUMN_FAVORITES + " integer, ");
		sb.append(COLUMN_RETWEETS + " integer, ");
		sb.append(COLUMN_TIME + " text);");

		try {
			db.execSQL(sb.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	static public void onUpgrade(SQLiteDatabase db, int oldVersion,
			int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
		SavedTweetsTable.onCreate(db);
	}
}
