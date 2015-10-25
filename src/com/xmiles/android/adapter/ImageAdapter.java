package com.xmiles.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.xmiles.android.R;

public class ImageAdapter extends BaseAdapter {
	private Context context;
	private final String[] mobileValues;

	public ImageAdapter(Context context, String[] mobileValues) {
		this.context = context;
		this.mobileValues = mobileValues;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View gridView;

		if (convertView == null) {

			gridView = new View(context);

			// get layout from mobile.xml
			gridView = inflater.inflate(R.layout.profile_fgmt_gridview_custom_items, null);

			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.grid_item_label);
			textView.setText(mobileValues[position]);

			// set image based on selected text
			ImageView imageView = (ImageView) gridView
					.findViewById(R.id.grid_item_image);

			String mobile = mobileValues[position];

			if (mobile.equals("Mapa")) {
				imageView.setImageResource(R.drawable.livefleet);
			} else if (mobile.equals("Histórico")) {
				imageView.setImageResource(R.drawable.report);
			}
			/*
			if (mobile.equals("Ranking")) {
				imageView.setImageResource(R.drawable.livefleet);
			} else if (mobile.equals("Histórico")) {
				imageView.setImageResource(R.drawable.report);
			} else if (mobile.equals("Rotas")) {
				imageView.setImageResource(R.drawable.alerts);
			} else {
				imageView.setImageResource(R.drawable.dashboard);
				//imageView.setImageResource(R.drawable.replay);
			}
			*/

		} else {
			gridView = (View) convertView;
		}

		return gridView;
	}

	@Override
	public int getCount() {
		return mobileValues.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}
