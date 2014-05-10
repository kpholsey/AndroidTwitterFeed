/*
 * James Keller
 * Kenneth Holsey
 * ITCS 4180 - 091
 * HW5
 * 4/16/14
 */

package edu.uncc.itcs4180;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.gson.Gson;

import edu.uncc.itcs.R;

public class TwitterListActivity extends Activity {
	protected String screenName;
	protected Twitter stream;
	protected static ProgressDialog dialog;
	final static String KEY_DETAILED_TWEET = "Detailed Tweet";
	private AlertDialog simpleAlert;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_twitter_list);

		// Creates a progress dialog while the user's Twitter stream is being
		// fetched by an async task
		dialog = new ProgressDialog(TwitterListActivity.this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.setMessage("Fetching Recent Tweets...");
		dialog.show();
		// Creation of an alert dialog to be used during exception handling
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Alert: Connectivity Status")
				.setMessage("Make sure that you have a network connection!")
				.setCancelable(false)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
		// Alert dialog instantiation
		simpleAlert = builder.create();
		// Grabs the intent sent for the MainActivity so that the stream
		// for that username can be collected
		screenName = getIntent().getExtras().getString(
				MainActivity.KEY_SCREENNAME);
		// Begins the collection process for the user's stream
		setTwitterStream(screenName);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.twitter_list, menu);
		return true;
	}
	
	

	public void setTwitterStream(String screenName) {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

		if (networkInfo != null && networkInfo.isConnected()) {
			new AsyncTwitterTask().execute(screenName);
		} else {
			simpleAlert.show();
		}
	}

	// Uses an AsyncTask to download a Twitter user's timeline
	// Adapted from the tutorial provided with the assignment by rockncoder
	private class AsyncTwitterTask extends AsyncTask<String, Void, String> {
		final static String CONSUMER_KEY = "RhVHlwheYC4VcARgAwgZ5HkZp";
		final static String CONSUMER_SECRET = "oOJeP0SA3ObiJFlRiHaVEdrUsNxDasX3o1lXsYq1TajjgiUMZT";
		final static String TwitterTokenURL = "https://api.twitter.com/oauth2/token";
		final static String TwitterStreamURL = "https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=";

		@Override
		protected String doInBackground(String... screenNames) {
			String result = null;

			if (screenNames.length > 0) {
				result = getTwitterStream(screenNames[0]);
			}
			return result;
		}

		// onPostExecute convert the JSON results into a Twitter object (which
		// is an Array list of tweets
		@Override
		protected void onPostExecute(String result) {
			stream = jsonToTwitter(result);
			dialog.dismiss();

			// Convert the stream into an array of type tweet
			Tweet[] tweets = stream.toArray(new Tweet[stream.size()]);
			// Send the stream to the listview and adapter
			ListView listView = (ListView) findViewById(R.id.listTwitterActivity);
			ListAdapter listAdapter = new TwitterListAdapter(
					TwitterListActivity.this, tweets);
			listView.setAdapter(listAdapter);

			// Setting the onitemclicklistener to create a detailed view of each
			// tweet when it is clicked in the list
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent detailedTweetIntent = new Intent(
							TwitterListActivity.this,
							DetailedTweetActivity.class);
					detailedTweetIntent.putExtra(KEY_DETAILED_TWEET,
							stream.get(position));
					startActivity(detailedTweetIntent);
				}
			});
		}

		// Converts a string of JSON data into a Twitter object
		private Twitter jsonToTwitter(String result) {
			Twitter twits = null;
			if (result != null && result.length() > 0) {
				try {
					Gson gson = new Gson();
					twits = gson.fromJson(result, Twitter.class);
				} catch (IllegalStateException ex) {
					// just eat the exception
				}
			}
			return twits;
		}

		// Convert a JSON authentication object into an Authenticated object
		private Authenticated jsonToAuthenticated(String rawAuthorization) {
			Authenticated auth = null;
			if (rawAuthorization != null && rawAuthorization.length() > 0) {
				try {
					Gson gson = new Gson();
					auth = gson.fromJson(rawAuthorization, Authenticated.class);
				} catch (IllegalStateException ex) {
					// just eat the exception
				}
			}
			return auth;
		}

		private String getResponseBody(HttpRequestBase request) {
			StringBuilder sb = new StringBuilder();
			try {

				DefaultHttpClient httpClient = new DefaultHttpClient(
						new BasicHttpParams());
				HttpResponse response = httpClient.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				String reason = response.getStatusLine().getReasonPhrase();

				if (statusCode == 200) {

					HttpEntity entity = response.getEntity();
					InputStream inputStream = entity.getContent();

					BufferedReader bReader = new BufferedReader(
							new InputStreamReader(inputStream, "UTF-8"), 8);
					String line = null;
					while ((line = bReader.readLine()) != null) {
						sb.append(line);
					}
				} else {
					sb.append(reason);
				}
			} catch (UnsupportedEncodingException ex) {
			} catch (ClientProtocolException ex1) {
			} catch (IOException ex2) {
			}
			return sb.toString();
		}

		private String getTwitterStream(String screenName) {
			String results = null;

			// Step 1: Encode consumer key and secret
			try {
				// URL encode the consumer key and secret
				String urlApiKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
				String urlApiSecret = URLEncoder.encode(CONSUMER_SECRET,
						"UTF-8");

				// Concatenate the encoded consumer key, a colon character, and
				// the encoded consumer secret
				String combined = urlApiKey + ":" + urlApiSecret;

				// Base64 encode the string
				String base64Encoded = Base64.encodeToString(
						combined.getBytes(), Base64.NO_WRAP);

				// Step 2: Obtain a bearer token
				HttpPost httpPost = new HttpPost(TwitterTokenURL);
				httpPost.setHeader("Authorization", "Basic " + base64Encoded);
				httpPost.setHeader("Content-Type",
						"application/x-www-form-urlencoded;charset=UTF-8");
				httpPost.setEntity(new StringEntity(
						"grant_type=client_credentials"));
				String rawAuthorization = getResponseBody(httpPost);
				Authenticated auth = jsonToAuthenticated(rawAuthorization);

				// Applications should verify that the value associated with the
				// token_type key of the returned object is bearer
				if (auth != null && auth.token_type.equals("bearer")) {

					// Step 3: Authenticate API requests with bearer token
					HttpGet httpGet = new HttpGet(TwitterStreamURL + screenName);

					// Construct a normal HTTPS request and include an
					// Authorization
					// Header with the value of Bearer <>
					httpGet.setHeader("Authorization", "Bearer "
							+ auth.access_token);
					httpGet.setHeader("Content-Type", "application/json");
					// Update the results with the body of the response
					results = getResponseBody(httpGet);
				}
			} catch (UnsupportedEncodingException ex) {
			} catch (IllegalStateException ex1) {
			}
			return results;
		}
	}

}
