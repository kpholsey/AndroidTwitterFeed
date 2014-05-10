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
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import edu.uncc.itcs.R;

public class MainActivity extends Activity {
	protected ListView screenNames;
	protected ListAdapter adapter;
	protected Button viewNews;
	protected Button clearNews;
	final static String KEY_SCREENNAME = "Screen Name";
	private static DatabaseDataManager dataManager;
	List<SavedTweet> peek;
	private AlertDialog simpleAlert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Creating arrays to hold the labels and usernames for the Twitter
		// account
		final String[] twitterLabels = { getString(R.string.cnn_label),
				getString(R.string.nytimes_label),
				getString(R.string.nba_label), getString(R.string.bbc_label),
				getString(R.string.techcrunch_label),
				getString(R.string.recode_label),
				getString(R.string.hackernews_label),
				getString(R.string.pando_label) };
		final String[] twitterScreenNames = {
				getString(R.string.cnn_screenname),
				getString(R.string.nytimes_screenname),
				getString(R.string.nba_screenname),
				getString(R.string.bbc_screenname),
				getString(R.string.techcrunch_screenname),
				getString(R.string.recode_screenname),
				getString(R.string.hackernews_screenname),
				getString(R.string.pando_screenname) };
		dataManager = new DatabaseDataManager(getBaseContext());
		// Creation of an alert dialog to be used during exception handling
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Alert: Saved Tweet Status")
				.setMessage("There are no saved tweets to display.")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		// Alert dialog instantiation
		simpleAlert = builder.create();
		screenNames = (ListView) findViewById(R.id.savedTweetsListView);
		// Creating a simple adapter to display the Twitter streams available
		adapter = new ArrayAdapter<String>(this, R.layout.feed_list,
				R.id.feedName, twitterLabels);
		screenNames.setAdapter(adapter);
		screenNames.setOnItemClickListener(new OnItemClickListener() {
			// When a list item is clicked the Twitter List Activity is started
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String screenName = twitterScreenNames[position];
				Intent twitterListActivity = new Intent(MainActivity.this,
						TwitterListActivity.class);
				twitterListActivity.putExtra(KEY_SCREENNAME, screenName);
				startActivity(twitterListActivity);
			}
		});

		// Buttons that clear tweets and display tweets stored in the
		// database
		viewNews = (Button) findViewById(R.id.savedNewsButton);
		viewNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Tests to see if any tweets are in the database
				peek = dataManager.getAllSavedTweets();
				if (peek.size() > 0) {
					Intent savedNewsIntent = new Intent(MainActivity.this,
							SavedNewsActivity.class);
					startActivity(savedNewsIntent);
				} else {
					simpleAlert.show();
				}
			}
		});

		clearNews = (Button) findViewById(R.id.button2);
		clearNews.setOnClickListener(new OnClickListener() {
			// Deletes all of the tweets in the database
			@Override
			public void onClick(View v) {
				dataManager.deleteAllSavedTweets();
				Toast.makeText(getBaseContext(),
						"All Saved Tweets Have Been Deleted",
						Toast.LENGTH_SHORT).show();
			}
		});

	}

	// Closes the database manager instance when the app is closed
	@Override
	protected void onDestroy() {
		dataManager.close();
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
