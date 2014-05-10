/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import edu.uncc.itcs.R;

public class DetailedTweetActivity extends Activity {
	Tweet singleTweet;
	TextView screenName;
	TextView tweetText;
	Button backButton;
	ImageView profileBackground;
	TextView favoritesCount;
	TextView retweetCount;
	BitmapFactory.Options options = new BitmapFactory.Options();
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_tweet);
		// Bitmapfactory options that allow the background image
		// to be downsized to avoid memory issues
		options.inSampleSize = 4;
		options.inPurgeable = true;

		// Captures the intent passed from the TwitterListActivity
		// The passed tweet allows the detailed information to be set to the
		// appropriate textviews and imageviews
		singleTweet = (Tweet) getIntent().getExtras().getSerializable(
				TwitterListActivity.KEY_DETAILED_TWEET);
		// Binding textviews and imageviews
		screenName = (TextView) findViewById(R.id.screenName);
		tweetText = (TextView) findViewById(R.id.tweetText);
		favoritesCount = (TextView) findViewById(R.id.favoritesCount);
		retweetCount = (TextView) findViewById(R.id.retweetCount);
		profileBackground = (ImageView) findViewById(R.id.profileBackground);
		backButton = (Button) findViewById(R.id.savedNewsButton);
		// Determines if the NY Times is the current screenname for the tweet
		// and sets the bitmap image quality lower to help avoid memory issues
		if(singleTweet.getUser().getScreenName().equals("nytimes")) {
			options.inSampleSize = 10;
		}
		screenName.setText(singleTweet.getUser().getName());
		tweetText.setText(singleTweet.getText());
		favoritesCount.setText("Favorites Count: "
				+ singleTweet.getFavoriteCount());
		retweetCount.setText("Retweet Count: " + singleTweet.getRetweetCount());

		// Creates an async task that is used to grab the background images
		// of the user for each tweet
		new GetBackgroundImageAsyncTask().execute(singleTweet.getUser()
				.getProfileBackgroundImageUrl());

		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.detailed_tweet, menu);
		return true;
	}

	// Simple async task for fetching the background profile URL's
	public class GetBackgroundImageAsyncTask extends
			AsyncTask<String, Void, Bitmap> {

		@Override
		protected Bitmap doInBackground(String... params) {
			String imgUrl = params[0];
			Bitmap image = null;
			URL url;
			try {
				url = new URL(imgUrl);
				image = BitmapFactory.decodeStream(url.openStream(), null, options);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return image;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			profileBackground.setImageBitmap(result);
		}
	}

}
