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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import edu.uncc.itcs.R;

public class TwitterListAdapter extends ArrayAdapter<Tweet> {
	protected Context myContext;
	protected Tweet[] stream;
	private static DatabaseDataManager dataManager;
	BitmapFactory.Options options;

	// Constructor used to setup context and captures the Twitter stream to be
	// displayed
	public TwitterListAdapter(Context context, Tweet[] stream) {
		super(context, R.layout.tweet_list_item, stream);
		myContext = context;
		this.stream = stream;
		dataManager = new DatabaseDataManager(myContext);
		// Bitmapfactory settings help to downsize images and
		// free up memory when creating the listview
		options = new BitmapFactory.Options();
		options.inSampleSize = 1;
		options.inPurgeable = true;
	}

	// Creates the views that will populate the list. Binds imageviews and
	// textviews
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		// Grabs the current tweet in the stream
		Tweet currentTweet = stream[position];

		if (convertView == null) {
			convertView = LayoutInflater.from(myContext).inflate(
					R.layout.tweet_list_item, null);
			holder = new ViewHolder();
			holder.profilePic = (ImageView) convertView
					.findViewById(R.id.profileBackground);
			holder.tweetMessage = (TextView) convertView
					.findViewById(R.id.screenName);
			holder.saveTweet = (ImageView) convertView
					.findViewById(R.id.imageView3);
			holder.retweetStatus = (ImageView) convertView
					.findViewById(R.id.imageView2);
			holder.date = (TextView) convertView.findViewById(R.id.textView2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		// Uses an asynctask to fetch the profile images for the stream
		new GetImageAsyncTask(holder.profilePic).execute(currentTweet.getUser()
				.getProfileImageUrl());
		holder.tweetMessage.setText(currentTweet.getText());
		holder.date.setText(currentTweet.getDateCreated());
		// Changes retweet image based on number of retweets
		if (currentTweet.getRetweetCount() > 0) {
			holder.retweetStatus.setImageDrawable(myContext.getResources()
					.getDrawable(R.drawable.retweeted));
		} else {
			holder.retweetStatus.setImageDrawable(myContext.getResources()
					.getDrawable(R.drawable.not_retweeted));
		}

		// Sets the tag for the current view with the current tweet to be used
		// as a reference
		// in the setonclicklistener...avoids scoping issues
		holder.saveTweet.setTag(currentTweet);
		holder.saveTweet.setOnClickListener(new OnClickListener() {

			// Creates and saves a new tweet to the database when clicked
			@Override
			public void onClick(View view) {
				Tweet savingTweet = (Tweet) view.getTag();
				SavedTweet savedTweet = new SavedTweet();
				savedTweet.setUserName(savingTweet.getUser().getName());
				savedTweet.setText(savingTweet.getText());
				savedTweet.setProfileBackgroundImage(savingTweet.getUser()
						.getProfileBackgroundImageUrl());
				savedTweet.setFavoriteCount(savingTweet.getFavoriteCount());
				savedTweet.setRetweetsCount(savingTweet.getRetweetCount());
				savedTweet.setTime(savingTweet.getDateCreated());
				dataManager.saveSavedTweet(savedTweet);
				Toast.makeText(myContext, "Saved in DB!", Toast.LENGTH_SHORT)
						.show();
			}
		});
		return convertView;
	}

	public static class ViewHolder {
		ImageView profilePic;
		TextView tweetMessage;
		ImageView saveTweet;
		ImageView retweetStatus;
		TextView date;
	}

	// Async task used to fetch the profile images for the current stream
	// Constructor allows each imageview to be set with a child thread
	public class GetImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
		ImageView currentImageView;
		//Constructor determines the correct view for the retrieved image
		public GetImageAsyncTask(ImageView imageView) {
			currentImageView = imageView;
		}

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
			currentImageView.setImageBitmap(result);
		}
	}
}
