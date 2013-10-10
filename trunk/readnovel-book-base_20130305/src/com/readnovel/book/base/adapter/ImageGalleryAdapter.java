package com.readnovel.book.base.adapter;

import com.readnovel.book.base.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

public class ImageGalleryAdapter extends BaseAdapter{
	private Context mContext;
	private int[] resIds = new int[]
			{ R.drawable.g_1, R.drawable.g_2, R.drawable.g_3, R.drawable.g_4
			 };
	int mGalleryItemBackground;
	public ImageGalleryAdapter(Context context)
	{
	mContext = context;
	}
	@Override
	public int getCount() {
		return resIds.length;
	}

	@Override
	public Object getItem(int position) {
		return resIds[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView imageView = new ImageView(mContext);
		imageView.setImageDrawable(mContext.getResources().getDrawable(resIds[position]));
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new Gallery.LayoutParams(70, 70));
		return imageView;
	}
}
