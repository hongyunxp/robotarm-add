package com.bus3.test;

import java.util.List;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bus3.R;

public class ImageGroupAdapter extends BaseAdapter {
	private List<Integer> picList;

	private Activity act;

	public ImageGroupAdapter(Activity act, List<Integer> picList) {
		this.act = act;
		this.picList = picList;
	}

	@Override
	public int getCount() {
		return picList.size();
	}

	@Override
	public Object getItem(int position) {
		return picList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView img = new ImageView(act, null, R.style.my_image_view_gallery);
		// img.setScaleType(ScaleType.FIT_XY);
		// img.setVisibility(View.GONE);
		// //
		// LayoutParams lp = new LayoutParams(150, 150);
		// img.setLayoutParams(lp);

		img.setImageBitmap(BitmapFactory.decodeResource(act.getResources(), picList.get(position)));

		return img;
	}

}
