package com.xmiles.android.listviewfeed;

import com.xmiles.android.listviewfeed.FeedImageView;
import com.xmiles.android.Hashtag;
import com.xmiles.android.R;
import com.xmiles.android.Relationship;
import com.xmiles.android.listviewfeed.AppController;
import com.xmiles.android.listviewfeed.FeedItem;
import com.xmiles.android.scheduler.Likes_Inbox_Upload;

import com.xmiles.android.sqlite.contentprovider.SqliteProvider;
import com.xmiles.android.sqlite.helper.DatabaseHelper;
import com.xmiles.android.support.ConnectionDetector;
import com.xmiles.android.support.Support;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class HashtagListAdapter extends BaseAdapter {
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	//TAG
	private static final String TAG = "FACEBOOK";

	// Connection detector
	ConnectionDetector cd;


	private static final Integer KEY_ID_PROFILE  = 0;
	private static final Integer KEY_ID         = 1;
	private static final Integer KEY_NAME       = 2;
	private static final Integer KEY_IMAGE      = 3;
	private static final Integer KEY_STATUS     = 4;
	private static final Integer KEY_PICURL     = 5;
	private static final Integer KEY_TIME_STAMP = 6;
	private static final Integer KEY_URL        = 7;
	private static final Integer KEY_CUSTOM_TIME_STAMP = 8;
	//---------------------
	private static final Integer KEY_LIKE_STATS = 9;
	private static final Integer KEY_COMMENT_STATS = 10;
	//---------------------
	private static final Integer KEY_YOU_LIKE_THIS = 11;
	private static final Integer KEY_SENDER = 12;
	private static final Integer KEY_FEED_TYPE = 13;
	//---------------------
	private static final Integer KEY_HASHTAG = 14;


	public HashtagListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
	}

	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView name = (TextView) convertView.findViewById(R.id.name);
		//final TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);
		TextView statusMsg = (TextView) convertView.findViewById(R.id.txtStatusMsg);
		//-------------------
		TextView hashtag_1b = (TextView) convertView.findViewById(R.id.hashtag_1);

		hashtag_1b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		//-------------------
		TextView hashtag_2 = (TextView) convertView.findViewById(R.id.hashtag_2);

		hashtag_2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});

		//-------------------
		TextView rel_stats = (TextView) convertView.findViewById(R.id.rel_stats);

		rel_stats.setTag(position);

		rel_stats.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					cd = new ConnectionDetector(activity.getApplicationContext());

					// Check if Internet present
					if (!cd.isConnectingToInternet()) {

				        //----do something---
						Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

				     } else {


						 int pos_rel_stats=(Integer)v.getTag();

						 //Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
						 //Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);
						 Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED_BY_HASHTAG;
						 Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);


						 data_newsfeed.moveToPosition(pos_rel_stats);


					     //-------------------------------------------
					     Bundle args = new Bundle();

					     args.putInt("position", pos_rel_stats);

					     //Log.i(TAG,"feed_id: " + data_newsfeed.getString(KEY_ID));

					     args.putString("feed_id", data_newsfeed.getString(KEY_ID));
					     args.putString("adapter", "hashtag");

					     Intent intent = new Intent(activity, Relationship.class);
					     intent.putExtras(args);

					     activity.startActivity(intent);
				     }
				}


			});

		//-------------------
		TextView url = (TextView) convertView.findViewById(R.id.txtUrl);
		NetworkImageView profilePic = (NetworkImageView) convertView
				.findViewById(R.id.profilePic);
		FeedImageView feedImageView = (FeedImageView) convertView
				.findViewById(R.id.feedImage1);

		Button like = (Button) convertView.findViewById(R.id.Button_like);

		like.setTag(position);
		like.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				cd = new ConnectionDetector(activity.getApplicationContext());

				// Check if Internet present
				if (!cd.isConnectingToInternet()) {

			        //----do something---
					Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

		        } else {



					int pos_btn_like = (Integer)v.getTag();

					//Uri uri_1 = SqliteProvider.CONTENT_URI_NEWSFEED;
					Uri uri_1 = SqliteProvider.CONTENT_URI_NEWSFEED_BY_HASHTAG;
					Cursor data_newsfeed = activity.getContentResolver().query(uri_1, null, null, null, null);
					data_newsfeed.moveToPosition(pos_btn_like);
					//------------------------------------------------------------
					//------------------------------------------------------------
					//Uri uri_2 = SqliteProvider.CONTENT_URI_NEWSFEED_update;
					Uri uri_2 = SqliteProvider.CONTENT_URI_NEWSFEED_BY_HASHTAG_update;
					Uri uri_2_b = SqliteProvider.CONTENT_URI_NEWSFEED_update;
					//------------------------------------------------------------
					ContentValues cv_1 = new ContentValues();

					int like_stats = Integer.parseInt(data_newsfeed.getString(KEY_LIKE_STATS));

					cv_1.put(DatabaseHelper.KEY_LIKE_STATS, String.valueOf(like_stats + 1));
					//------
					cv_1.put(DatabaseHelper.KEY_YOU_LIKE_THIS, "YES");
					//-----
					activity.getContentResolver().update(uri_2,
							cv_1,
							DatabaseHelper.KEY_ID + " = " + data_newsfeed.getString(KEY_ID), null);
					//-----
					activity.getContentResolver().update(uri_2_b,
							cv_1,
							DatabaseHelper.KEY_ID + " = " + data_newsfeed.getString(KEY_ID), null);

					//------------------------------------------------------------
					//------------------------------------------------------------
					Uri uri_3 = SqliteProvider.CONTENT_URI_USER_PROFILE;
					Cursor data_profile = activity.getContentResolver().query(uri_3, null, null, null, null);
					data_profile.moveToFirst();

					Uri uri_4 = SqliteProvider.CONTENT_URI_LIKES_UPLOAD;
					Cursor data_likes = activity.getContentResolver().query(uri_4, null, null, null, null);

					ContentValues cv_2 = new ContentValues();

					//feed_id
					cv_2.put(DatabaseHelper.KEY_ID, data_newsfeed.getString(KEY_ID));
					//user_id
					cv_2.put(DatabaseHelper.KEY_U_ID, data_profile.getString(KEY_ID_PROFILE));
					// flag_action
					cv_2.put(DatabaseHelper.KEY_FLAG_ACTION, "ADD");
					// time_stamp
					Support support = new Support();
					cv_2.put(DatabaseHelper.KEY_TIME_STAMP, support.getDateTime());
					//sender
					cv_2.put(DatabaseHelper.KEY_SENDER, data_newsfeed.getString(KEY_SENDER));
					//status
					cv_2.put(DatabaseHelper.KEY_STATUS, data_newsfeed.getString(KEY_STATUS));
					//feed_type
					cv_2.put(DatabaseHelper.KEY_FEED_TYPE, data_newsfeed.getString(KEY_FEED_TYPE));

					//---------------------------------------
					//-----------------------------
			    	Intent intent=new Intent("feedfragmentupdater");
			    	activity.sendBroadcast(intent);
					//-----------------------------
					//---------------------------------------

					//if (data_likes.getCount() == 0) {
						//cv_2.put(DatabaseHelper.KEY_FLAG_ACTION, "ADD");
						//Log.e(TAG,"KEY_FLAG_ACTION: " + "ADD");
						//-------
						Uri uri_5 = SqliteProvider.CONTENT_URI_LIKES_UPLOAD_insert;
						activity.getContentResolver().insert(uri_5, cv_2);

						//-------
						Likes_Inbox_Upload liu = new Likes_Inbox_Upload();
						liu.setAlarm(activity);
						//-------
					//} else {
						//cv_2.put(DatabaseHelper.KEY_FLAG_ACTION, "WAIT");
						////Log.i(TAG,"KEY_FLAG_ACTION: " + "WAIT");
	                	//DatabaseHelper mDatabaseHelper;
	                	//mDatabaseHelper = new DatabaseHelper(activity);
	                	//mDatabaseHelper.resetLikes_upload();
					//}


					//------------------------------------------------------------
					//------------------------------------------------------------

					//---------------
					feedItems.clear();
					//---------------------------
					Cursor newsfeed = activity.getContentResolver().query(uri_1, null, null, null, null);

					for (int i = 0; i < newsfeed.getCount(); i++) {

						newsfeed.moveToPosition(i);

						FeedItem item = new FeedItem();

						item.setId(newsfeed.getInt(KEY_ID));
						item.setName(newsfeed.getString(KEY_NAME));

						// Image might be null sometimes
						String image = newsfeed.isNull(KEY_IMAGE) ? null : newsfeed
								.getString(KEY_IMAGE);

						item.setImge(image);

						item.setStatus(newsfeed.getString(KEY_STATUS));
						item.setProfilePic(newsfeed.getString(KEY_PICURL));

						// like, comments stats
						item.setLike_stats(newsfeed.getString(KEY_LIKE_STATS));
						item.setComment_stats(newsfeed.getString(KEY_COMMENT_STATS));

						//you_like_this
						item.setYou_like_this(newsfeed.getString(KEY_YOU_LIKE_THIS));


						if (newsfeed.isNull(KEY_CUSTOM_TIME_STAMP)) {
							item.setTimeStamp(newsfeed.getString(KEY_TIME_STAMP));
						} else {
							item.setTimeStamp(newsfeed.getString(KEY_CUSTOM_TIME_STAMP));
						}

						// url might be null sometimes
						String feedUrl = newsfeed.isNull(KEY_URL) ? null : newsfeed
								.getString(KEY_URL);

						item.setUrl(feedUrl);

						// hashtag might be null sometimes
						String hashtag = newsfeed.isNull(KEY_HASHTAG) ? null : newsfeed
								.getString(KEY_HASHTAG);

						item.setHashtag_1(hashtag);

						feedItems.add(item);
					}

					//---------------------------
					notifyDataSetChanged();
					//---------------
		        }
			}
		});
		//-------------------
		Button comment = (Button) convertView.findViewById(R.id.Button_comment);

		comment.setTag(position);
		comment.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				cd = new ConnectionDetector(activity.getApplicationContext());

				// Check if Internet present
				if (!cd.isConnectingToInternet()) {

			        //----do something---
					Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.internet_connection), Toast.LENGTH_SHORT).show();

		        } else {


					int pos_btn_comment = (Integer)v.getTag();
					//Log.e(TAG,"pos_btn_comment: " + pos_btn_comment);

					//Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED;
					//Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);
					Uri uri = SqliteProvider.CONTENT_URI_NEWSFEED_BY_HASHTAG;
					Cursor data_newsfeed = activity.getContentResolver().query(uri, null, null, null, null);



					data_newsfeed.moveToPosition(pos_btn_comment);

				    Bundle args = new Bundle();
				    args.putInt("position", pos_btn_comment);

				    args.putString("feed_id", data_newsfeed.getString(KEY_ID));
				    args.putString("adapter", "hashtag");

				    Intent intent = new Intent(activity, Relationship.class);
				    intent.putExtras(args);

				    activity.startActivity(intent);
				    //-------------
				    //activity.finish();
				    //-------------
		        }
			}
		});


		//-------------------
		FeedItem item = feedItems.get(position);

		name.setText(item.getName());

		// Check for empty like, comments stats
	     int like_stats = Integer.parseInt(item.getLike_stats());
	     int comm_stats = Integer.parseInt(item.getComment_stats());

	     if (like_stats + comm_stats == 0){
			 // rel_stats is empty, remove from view
	    	 rel_stats.setVisibility(View.GONE);

	     } else {
	    	 rel_stats.setVisibility(View.VISIBLE);
	 		// like, comments stats
	 		rel_stats.setText(item.getLike_stats() + " curtida(s) " +  item.getComment_stats() + " comentário(s)");

	     }

	     //Like Button
	     if (item.getYou_like_this().equals("YES")){

	    	 //like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_active_new_green, 0, 0, 0);
	    	 //like.setTextColor(activity.getResources().getColor(R.color.green_dark));
	    	 like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_icon_active_new_orange_20, 0, 0, 0);
	    	 like.setTextColor(activity.getResources().getColor(R.color.ab_mid));

	    	 like.setText("Curtiu");
	    	 like.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
	    	 like.setEnabled(false);

	     } else {

	    	 like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.like_light_grey_l, 0, 0, 0);
	    	 like.setTextColor(activity.getResources().getColor(R.color.timestamp));
	    	 like.setText("Curtir");
	    	 like.setTextSize(TypedValue.COMPLEX_UNIT_DIP,13);
	    	 //like.setEnabled(true);

	    	 if (item.getId() > -1){
	    		 like.setEnabled(true);
	    		 comment.setEnabled(true);
	    	 } else {
	    		 like.setEnabled(false);
	    		 comment.setEnabled(false);
	    	 }
	     }



		Support support = new Support();

		// Converting timestamp into x ago format
		CharSequence timeAgo = DateUtils.getRelativeTimeSpanString(
				Long.parseLong(support.getDateTime_long(item.getTimeStamp())),
				System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
		timestamp.setText(timeAgo);


		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getStatus())) {

			if (item.getStatus().indexOf("<bold>") != -1){
				/*
				statusMsg.setText(Html.fromHtml(item.getStatus().split("<bold>")[0] +
						                       "<b>" + item.getStatus().split("<bold>")[1] + "</b>" +
						                       item.getStatus().split("<bold>")[2]));
				*/
				String args[]  = item.getStatus().split("<bold>");

				if (args.length == 2){

					statusMsg.setText(Html.fromHtml(item.getStatus().split("<bold>")[0] +
		                       "<b>" + item.getStatus().split("<bold>")[1] + "</b>"));

				} else {

					statusMsg.setText(Html.fromHtml(item.getStatus().split("<bold>")[0] +
							                       "<b>" + item.getStatus().split("<bold>")[1] + "</b>" +
							                       item.getStatus().split("<bold>")[2]));
				}



			} else {
				statusMsg.setText(item.getStatus());
			}

			//statusMsg.setText(item.getStatus());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}

		// Checking for null feed url
		//if (item.getUrl() != null) {
		if (item.getUrl() != null && !item.getUrl().equals("")) {
			url.setText(Html.fromHtml("<a href=\"" + item.getUrl() + "\">"
					+ item.getUrl() + "</a> "));

			// Making url clickable
			url.setMovementMethod(LinkMovementMethod.getInstance());
			url.setVisibility(View.VISIBLE);
		} else {
			/*
			if (item.getName().equals("xMiles")){


				// Making url clickable
				url.setMovementMethod(LinkMovementMethod.getInstance());
				url.setVisibility(View.VISIBLE);

			} else { */
				// url is null, remove from the view
				url.setVisibility(View.GONE);
			//}
		}

		// Checking for null hashtag
		if (item.getHashtag_1() != null && !item.getHashtag_1().equals("")) {
		//if (item.gethashtag_1b().split(",")[0] != null
		//		&& !item.gethashtag_1b().split(",")[0].equals("")) {

			//hashtag_1b.setText(item.gethashtag_1b());
			hashtag_1b.setText(item.getHashtag_1().split(",")[0]);
			hashtag_1b.setVisibility(View.VISIBLE);

			if (item.getHashtag_1().split(",").length > 1) {

				////Log.e(TAG,"item.gethashtag_1b().split(,)[1]: " + item.gethashtag_1b().split(",")[1]);

				hashtag_2.setText(item.getHashtag_1().split(",")[1]);
				hashtag_2.setVisibility(View.VISIBLE);
			} else {
				hashtag_2.setVisibility(View.GONE);
			}

		} else {

			// hashtag is null, remove from the view
			hashtag_1b.setVisibility(View.GONE);
			hashtag_2.setVisibility(View.GONE);
		}



		// user profile pic
		profilePic.setImageUrl(item.getProfilePic(), imageLoader);

		// Feed image
		//if (item.getImge() != null) {
		if (item.getImge() != null && !item.getImge().equals("")) {
			feedImageView.setImageUrl(item.getImge(), imageLoader);
			feedImageView.setVisibility(View.VISIBLE);
			feedImageView
					.setResponseObserver(new FeedImageView.ResponseObserver() {
						@Override
						public void onError() {
						}

						@Override
						public void onSuccess() {
						}
					});
		} else {
			feedImageView.setVisibility(View.GONE);
		}

		return convertView;
	}


}
