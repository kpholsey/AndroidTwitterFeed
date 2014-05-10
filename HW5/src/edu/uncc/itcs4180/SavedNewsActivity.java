/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import edu.uncc.itcs.R;

public class SavedNewsActivity extends Activity {
	private static DatabaseDataManager dataManager;
	
	//Displays a simple list view of saved tweets initiated from the
	//MainActivity
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_saved_news);
		dataManager = new DatabaseDataManager(getBaseContext());
		
		List<SavedTweet> savedTweets = dataManager.getAllSavedTweets();
		ListView myListView = (ListView) findViewById(R.id.savedTweetsListView);
		ArrayAdapter<SavedTweet> adapter = new ArrayAdapter<SavedTweet>(this,
				android.R.layout.simple_list_item_1, android.R.id.text1,
				savedTweets);
		myListView.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.saved_news, menu);
		return true;
	}

}
