package com.xmiles.android;

import static com.xmiles.android.pushnotifications.CommonUtilities.SENDER_ID;
import static com.xmiles.android.pushnotifications.CommonUtilities.displayMessage;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.xmiles.android.R;
import com.xmiles.android.R.drawable;
import com.xmiles.android.R.string;
import com.xmiles.android.pushnotifications.ServerUtilities;
import com.xmiles.android.sqlite.contentprovider.SqliteProvider;

public class GCMIntentService extends GCMBaseIntentService {

	private static final String TAG = "FACEBOOK";
	private static final Integer KEY_NAME = 1;
	private static final Integer KEY_U_ID = 0;
	private Cursor users_info;

    public GCMIntentService() {
        super(SENDER_ID);
    }

    /**
     * Method called on device registered
     **/
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        displayMessage(context, "Your device registred with GCM");
        //Log.d("NAME", MainActivity.name);
                
        Uri uri_1 = SqliteProvider.CONTENT_URI_USER_PROFILE;        
        users_info = context.getContentResolver().query(uri_1, null, null, null, null);
        users_info.moveToLast();
        
        Log.d(TAG, users_info.getString(KEY_NAME));
        
        //ServerUtilities.register(context, MainActivity.name, MainActivity.email, registrationId);

        ServerUtilities.register(context, users_info.getString(KEY_NAME), users_info.getString(KEY_U_ID), registrationId);
    }

    /**
     * Method called on device un registred
     * */
    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        displayMessage(context, getString(R.string.gcm_unregistered));
        
        ServerUtilities.unregister(context, registrationId);
    }

    /**
     * Method called on Receiving a new message
     * */
    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString("price");
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on receiving a deleted message
     * */
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_deleted, total);
        
        displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    /**
     * Method called on Error
     * */
    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        displayMessage(context, getString(R.string.gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        displayMessage(context, getString(R.string.gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.xmiles_logo_rev05_transparente;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        
        String msg = message.split(";")[1];
        String feed_id = message.split(";")[0];
        //String push_type = message.split(";")[0];
        
        //Log.i(TAG,"push_type: " + push_type);
        Log.v(TAG,"feed_id: " + feed_id);
        
        // Local to add picture from sender too
        Notification notification = new Notification(icon, msg, when);
        
        String title = context.getString(R.string.app_name);
        

        Intent notificationIntent = new Intent(context, Push.class);
        
        notificationIntent.putExtra("feed_id", feed_id);
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        //PendingIntent intent =
        //        PendingIntent.getActivity(context, 0, notificationIntent, 0);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        
        notification.setLatestEventInfo(context, title, msg, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        
        // Play default notification sound
        notification.defaults |= Notification.DEFAULT_SOUND;
        
        //notification.sound = Uri.parse("android.resource://" + context.getPackageName() + "your_sound_file_name.mp3");
        
        // Vibrate if vibrate is enabled
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);      

    }

}
