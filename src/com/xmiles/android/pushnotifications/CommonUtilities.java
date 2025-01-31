package com.xmiles.android.pushnotifications;

import android.content.Context;
import android.content.Intent;

public final class CommonUtilities {

	// give your server registration url here

    public static final String SERVER_URL = "http://ec2-54-163-172-76.compute-1.amazonaws.com/xmiles/gcm/register.php";

    // Google project id
    public static final String SENDER_ID = "485149707692";  // SenderID from PUSH NOTIFICATION By GCM (androidhive example)
    //public static final String SENDER_ID = "1372";  // SenderID from xMiles project @ Google APIs

    /**
     * Tag used on log messages.
     */
    static final String TAG = "FACEBOOK";

    //public static final String DISPLAY_MESSAGE_ACTION =
    //        "com.androidhive.pushnotifications.DISPLAY_MESSAGE";

    public static final String DISPLAY_MESSAGE_ACTION =
            "com.xmiles.android.DISPLAY_MESSAGE";


    public static final String EXTRA_MESSAGE = "message";

    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
}