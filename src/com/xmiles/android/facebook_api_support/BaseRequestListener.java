package com.xmiles.android.facebook_api_support;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import android.util.Log;

import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

/**
 * Skeleton base class for RequestListeners, providing default error handling.
 * Applications should handle these error conditions.
 */
public abstract class BaseRequestListener implements RequestListener {
	
	private static final String TAG = "FACEBOOK";

    @Override
    public void onFacebookError(FacebookError e, final Object state) {
        ////Log.e("Facebook", e.getMessage());
        //Log.e(TAG, "BaseRequestListener " + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onFileNotFoundException(FileNotFoundException e, final Object state) {
        ////Log.e("Facebook", e.getMessage());
        //Log.e(TAG, "BaseRequestListener " + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onIOException(IOException e, final Object state) {
        ////Log.e("Facebook", e.getMessage());
        //Log.e(TAG, "BaseRequestListener " + e.getMessage());
        e.printStackTrace();
    }

    @Override
    public void onMalformedURLException(MalformedURLException e, final Object state) {
        ////Log.e("Facebook", e.getMessage());
        //Log.e(TAG, "BaseRequestListener " + e.getMessage());
        e.printStackTrace();
    }

}
