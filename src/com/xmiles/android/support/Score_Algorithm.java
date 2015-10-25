package com.xmiles.android.support;

import com.xmiles.android.R;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

public class Score_Algorithm {

	private final Context mContext;	
	private static final String TAG = "FACEBOOK";
	
	public Score_Algorithm(Context ctx) {
		this.mContext = ctx;		
		//Log.w(TAG,"Score_Algorithm: " + "");
		
	}
	
	
	public void showBuscodeDialog(){
		
		Dialog buscode_dialog = new Dialog(mContext);
		buscode_dialog.setContentView(R.layout.buscode_dialog);
		buscode_dialog.setTitle("Digite o código do ônibus:");		
		buscode_dialog.setCancelable(true);

		//Button 
		buscode_dialog.show();

	}
}
